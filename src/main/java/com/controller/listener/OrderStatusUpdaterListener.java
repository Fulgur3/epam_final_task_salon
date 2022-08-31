package com.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class OrderStatusUpdaterListener implements ServletContextListener {
    private ScheduledExecutorService executorService;

    /**
     * Starts order status updater thread one a day
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new OrderStatusUpdaterThread(), 0, 24,
                TimeUnit.HOURS);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdownNow();
    }
}