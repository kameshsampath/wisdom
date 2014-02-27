package org.wisdom.test.parents;

import org.wisdom.api.http.Context;
import org.wisdom.api.http.Result;
import org.wisdom.api.http.Results;

/**
 * Allow configuring an invocation of an action.
 */
public class Action {

    private final Invocation invocation;
    private final FakeContext context;

    public Action(Invocation invocation) {
        this.invocation = invocation;
        this.context = new FakeContext();
    }

    public static Action action(Invocation invocation) {
        return new Action(invocation);
    }

    public Action with() {
        return this;
    }

    public Action parameter(String name, String value) {
        context.setParameter(name, value);
        return this;
    }

    public Action body(Object object) {
        context.setBody(object);
        return this;
    }

    public Action parameter(String name, int value) {
        context.setParameter(name, Integer.toString(value));
        return this;
    }

    public Action parameter(String name, boolean value) {
        context.setParameter(name, Boolean.toString(value));
        return this;
    }

    public Action attribute(String name, String value) {
        context.setAttribute(name, value);
        return this;
    }

    public Action header(String name, String value) {
        context.setHeader(name, value);
        return this;
    }

    public ActionResult invoke() {
        return _invoke();
    }

    private ActionResult _invoke() {
        // Set the fake context.
        Context.CONTEXT.set(context);
        // Create the request

        // Invoke
        try {
            return new ActionResult(
                    invocation.invoke(),
                    context);
        } catch (Throwable e) { //NOSONAR
            return new ActionResult(Results.internalServerError(e), context);
        } finally {
            Context.CONTEXT.remove();
        }
    }

    public static class ActionResult {

        private final Result result;
        private final Context context;

        public ActionResult(Result result, Context context) {
            this.result = result;
            this.context = context;
        }
        
        public Result getResult() {
            return result;
        }

        public Context getContext() {
            return context;
        }
    }
}
