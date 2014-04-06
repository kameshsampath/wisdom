package org.wisdom.monitor;

import com.google.common.collect.ImmutableMap;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.extender.InstanceDeclaration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.content.Json;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

import java.util.Collection;
import java.util.List;

/**
 * The controller providing the monitoring capabilities for iPOJO.
 */
@Controller
@Path("/monitor")
public class IPOJOController extends DefaultController {

    @Requires
    Json json;

    @View("ipojo")
    Template ipojo;

    @Context
    BundleContext context;

    private final static Logger LOGGER = LoggerFactory.getLogger(IPOJOController.class);


    @Route(method = HttpMethod.GET, uri = "/ipojo")
    public Result ipojo() {
        return ok(render(ipojo));
    }


    @Route(method = HttpMethod.GET, uri = "/ipojo.json")
    public Result bundles() {
        final List<InstanceModel> instances = InstanceModel.instances(context);
        final List<FactoryModel> factories = FactoryModel.factories(context);
        int valid = 0, invalid = 0, stopped = 0;
        for (InstanceModel model : instances) {
            if (model.getState().equals("VALID")) {
                valid++;
            } else if (model.getState().equals("INVALID")) {
                invalid++;
            } else if (model.getState().equals("STOPPED")) {
                stopped++;
            }
        }
        return ok(ImmutableMap.builder()
                .put("instances", instances)
                .put("factories", factories)
                .put("valid", valid)
                .put("invalid", invalid)
                .put("stopped", stopped)
                .put("unbound", Integer.toString(getUnboundDeclarationCount()))
                .build()).json();
    }

    private int getUnboundDeclarationCount() {
        int count = 0;
        try {
            Collection<ServiceReference<InstanceDeclaration>> list = context.getServiceReferences(InstanceDeclaration
                    .class, null);
            for (ServiceReference<InstanceDeclaration> ref : list) {
                InstanceDeclaration declaration = context.getService(ref);
                if (!declaration.getStatus().isBound()) {
                    count++;
                }
            }

        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        return count;
    }
}
