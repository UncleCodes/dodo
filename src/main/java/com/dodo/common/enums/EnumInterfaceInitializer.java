package com.dodo.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.dodo.utils.file.scanner.DodoAssignableFilter;
import com.dodo.utils.file.scanner.DodoFileScanner;

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
public class EnumInterfaceInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnumInterfaceInitializer.class);

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        ApplicationContext context = (ApplicationContext) arg0.getSource();
        if (context.getParent() == null) {
            LOGGER.info("Searching EnumInterfaces.......");
            ClassLoader loader = EnumInterfaceInitializer.class.getClassLoader();
            DodoAssignableFilter assignableFilter = new DodoAssignableFilter(EnumInterface.class);
            DodoFileScanner fileScanner = new DodoFileScanner(assignableFilter);
            fileScanner.find(loader, "", null, true);
            LOGGER.info("Searching EnumInterfaces...........OK");
            assignableFilter.getClassList().forEach(clazz -> {
                if (!clazz.isInterface()) {
                    LOGGER.info("Init EnumInterface Cache.......[{}]", clazz.getName());
                    EnumInterface.init((Class<? extends EnumInterface>) clazz);
                }
            });
        }
    }
}
