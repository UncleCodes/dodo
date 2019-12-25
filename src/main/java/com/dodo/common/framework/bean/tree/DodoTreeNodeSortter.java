package com.dodo.common.framework.bean.tree;

import java.util.Comparator;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoTreeNodeSortter implements Comparator<DodoTreeNode>{
	@Override
	public int compare(DodoTreeNode o1, DodoTreeNode o2) {
		return o1.getSortSeq()-o2.getSortSeq();
	}
}
