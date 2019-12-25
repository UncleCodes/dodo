package com.dodo.common.database.datasource;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DbChooser {
    public static void chooseDb(String dataSourceType) {
        MultiDataSourceSelector.set(dataSourceType);
    }
}
