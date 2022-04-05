package com.tracking.controllers.listeners;

import org.apache.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Request listener, that sets default language at the start of work
 */
@WebListener
public class RequestListener implements ServletRequestListener {

    private static final Logger logger = Logger.getLogger(RequestListener.class);


    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        setLang((HttpServletRequest) sre.getServletRequest());
        logger.info("Request initialized");
    }

    /**
     * Setting default language
     * @param req request, from which the session is obtained to get/set attribute
     */
    private void setLang(HttpServletRequest req) {
        if (req.getSession().getAttribute("lang") == null) {
            req.getSession().setAttribute("lang", "en_EN");
            logger.info("Language set: en_EN");
        }
    }
}
