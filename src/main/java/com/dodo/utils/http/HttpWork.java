package com.dodo.utils.http;

import org.apache.http.HttpResponse;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@FunctionalInterface
public interface HttpWork<T> {
    public T doWithResponse(HttpResponse response);
}
