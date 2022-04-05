package com.tracking.controllers.listeners;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Context listener, in which the necessary tools are initialized at the start of work
 */
@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initLog4j(sce.getServletContext());
        logger.info("Context initialized");
    }

    /**
     * Initialize Log4J logger
     * @param context servlet context, that helps to get full path of web project
     */
    private void initLog4j(ServletContext context) {
        String fullPath = context.getRealPath("") + "WEB-INF";
        String log4jPath = fullPath + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jPath);
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(log4jPath));
            prop.setProperty("root", fullPath);
            prop.store(new FileOutputStream(log4jPath), null);
            logger.info("Log4j initialized");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
