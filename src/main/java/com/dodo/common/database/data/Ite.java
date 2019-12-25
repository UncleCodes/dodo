package com.dodo.common.database.data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class Ite implements Iterator<Record>{
	private Iterator<Map<String, Object>> ite;
	public Ite(List<Map<String, Object>> rawData) {
		super();
		ite = rawData.iterator();
	}

	@Override
	public boolean hasNext() {
		return ite.hasNext();
	}
	@Override
	public Record next() {
		return new Record(ite.next());
	}

	@Override
	public void remove() {
		ite.remove();
	}
}
