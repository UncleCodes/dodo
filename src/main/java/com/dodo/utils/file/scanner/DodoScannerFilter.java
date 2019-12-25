package com.dodo.utils.file.scanner;

import java.net.URL;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface DodoScannerFilter {
	boolean filter(String urlStr, URL url, ClassLoader classLoader);
}
