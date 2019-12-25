package com.dodo.utils.web.id;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JdkUUIDGenerator implements SessionIdGenerator {
    public String get() {
        return StringUtils.remove(UUID.randomUUID().toString(), '-');
    }
}
