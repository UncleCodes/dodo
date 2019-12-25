package com.dodo.common.framework.destroyer;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.LoggerContext;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Component
public class LoggerContextDestroyer implements ApplicationListener<ContextClosedEvent>, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerContextDestroyer.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ApplicationContext context = (ApplicationContext) event.getSource();
        if (context.getParent() == null) {
            LOGGER.info("LoggerContext is closing..............");
            ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
            if (iLoggerFactory instanceof LoggerContext) {
                LoggerContext loggerContext = (LoggerContext) iLoggerFactory;
                loggerContext.stop();
            }
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}