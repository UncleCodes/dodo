package com.dodo.privilege.security;

import com.dodo.privilege.enums.FieldRightType;
import com.dodo.security.DodoPrincipalService;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
//TODO 权限考虑code
public abstract interface DodoSecurityService extends DodoPrincipalService {
    boolean hasRight(String path);

    boolean hasFieldRight(String entityFullName, String fieldName, FieldRightType type);

    void refreshCurrLoginAdmin();
}
