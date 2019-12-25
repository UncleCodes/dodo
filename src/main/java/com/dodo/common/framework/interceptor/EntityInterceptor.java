package com.dodo.common.framework.interceptor;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.dodo.common.framework.entity.BaseEntity;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class EntityInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 7319416231145791577L;

	public boolean onSave(Object obj, Serializable serializable,
			Object[] objects, String[] strings,
			Type[] types) {
		if ((obj instanceof BaseEntity)) {
			for (int i = 0; i < strings.length; i++) {
				if ("createDate".equals(strings[i])||"modifyDate".equals(strings[i])){
					objects[i] = new Date();
				}
			}
		}
		return true;
	}

	public boolean onFlushDirty(Object obj,
			Serializable serializable, Object[] objectss,
			Object[] objects, String[] strings,
			Type[] types) {
		if ((obj instanceof BaseEntity)) {
			for (int i = 0; i < strings.length; i++) {
				if ("modifyDate".equals(strings[i])){
					objectss[i] = new Date();
					break;
				}
			}
		}
		return true;
	}
}