package org.ylab;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.ylab.meter.MeterService;
import org.ylab.reading.ReadingService;
import org.ylab.user.UserService;

public class ContextListener implements ServletContextListener {
    /**
     * @param sce ServletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ManualConfig.setJdbcRepo();
        final ServletContext servletContext =
                sce.getServletContext();

        UserService userService = ManualConfig.getUserService();
        ReadingService readingServiceByUser = ManualConfig.getReadingServiceByUser();
        ReadingService readingServiceByAdmin = ManualConfig.getReadingServiceByAdmin();
        MeterService meterService = ManualConfig.getMeterService();

        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("readingServiceByUser", readingServiceByUser);
        servletContext.setAttribute("readingServiceByAdmin", readingServiceByAdmin);
        servletContext.setAttribute("meterService", meterService);
    }

    /**
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.closeConnection();
    }
}