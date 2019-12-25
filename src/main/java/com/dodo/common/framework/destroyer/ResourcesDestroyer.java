package com.dodo.common.framework.destroyer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

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
public class ResourcesDestroyer implements ApplicationListener<ContextClosedEvent>, BeanFactoryAware, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesDestroyer.class);

    private ListableBeanFactory beanFactory;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ApplicationContext context = (ApplicationContext) event.getSource();
        if (context.getParent() == null) {
            beanFactory.getBeansOfType(Destroyable.class).forEach((k, destroyer) -> {
                LOGGER.info("exec {}", destroyer);
                destroyer.destroy();
            });
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;

    }
}