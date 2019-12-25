package com.dodo.privilege.job.jobexec;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;

import com.dodo.utils.SpringUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JobExecListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        WebApplicationContext context = (WebApplicationContext) arg0.getSource();
        if (context.getParent() == null) {
            try {
                Map<String, JobExecutor> jobExecutors = SpringUtil.getBeansOfType(JobExecutor.class);
                for (JobExecutor jobExecutor : jobExecutors.values()) {
                    Method m = jobExecutor.getClass().getMethod("execute", new Class[0]);
                    m.invoke(jobExecutor, new Object[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
