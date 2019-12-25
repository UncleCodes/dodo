package com.dodo.common.framework.entity;

import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import com.dodo.common.framework.entity.id.IdWorker;
import com.dodo.common.framework.entity.id.IdWorkerImpl;
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
public class DodoIdGenerator implements IdentifierGenerator, Configurable {

    private IdWorker idWorker = null;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (idWorker == null) {
            try {
                idWorker = SpringUtil.getApplicationContext().getBean(IdWorker.class);
            } catch (Exception e) {
                idWorker = new IdWorkerImpl(new Random().nextInt((int) IdWorkerImpl.getMaxworkerid()),
                        new Random().nextInt((int) IdWorkerImpl.getMaxdatacenterid()));
            }
        }
        return idWorker.nextId().toString();
    }

}
