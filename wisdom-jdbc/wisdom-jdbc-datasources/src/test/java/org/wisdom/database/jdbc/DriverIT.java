package org.wisdom.database.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.jdbc.DataSourceFactory;
import org.ow2.chameleon.testing.helpers.OSGiHelper;
import org.wisdom.test.parents.WisdomTest;

import java.sql.Driver;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Check the loading and instantiation of JDBC drivers.
 */
public class DriverIT extends WisdomTest {

    private OSGiHelper helper;

    @Before
    public void setUp() {
        helper = new OSGiHelper(context);
    }

    @After
    public void tearDown() {
        helper.dispose();
    }

    @Test
    public void testMySQL() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/wisdom";

        DataSourceFactory factory = helper.getServiceObject(DataSourceFactory.class,
                "(" + DataSourceFactory.OSGI_JDBC_DRIVER_NAME + "=MySQL)");

        Driver driver = factory.createDriver(null);
        assertThat(driver).isNotNull();
        System.out.println("JDBC Driver " + driverClassName + " version " + driver.getMajorVersion() + "." + driver
                .getMinorVersion());
        assertThat(driver.acceptsURL(url)).isTrue();

        // Test with default attributes
        url = "jdbc:mysql://localhost/test?useUnicode=yes&characterEncoding=UTF-8&connectionCollation=utf8_general_ci";
        assertThat(driver.acceptsURL(url)).isTrue();
    }

    @Test
    public void testPostgreSQL() throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            SQLException {
        String driverClassName = "org.postgresql.Driver";
        String url = "jdbc:postgresql:wisdom";

        DataSourceFactory factory = helper.getServiceObject(DataSourceFactory.class,
                "(" + DataSourceFactory.OSGI_JDBC_DRIVER_NAME + "=PostgreSQL)");

        Driver driver = factory.createDriver(null);
        assertThat(driver).isNotNull();
        System.out.println("JDBC Driver " + driverClassName + " version " + driver.getMajorVersion() + "." + driver
                .getMinorVersion());
        assertThat(driver.acceptsURL(url)).isTrue();
    }

}
