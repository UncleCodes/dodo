package com.dodo.weixin.bean.menu;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MenuQueryBean extends ResultMsgBean{
	private MenuButtonsBean menu;

	public MenuButtonsBean getMenu() {
		return menu;
	}

	public void setMenu(MenuButtonsBean menu) {
		this.menu = menu;
	}

	@Override
	public String toString() {
		return super.toString()+"\nMenuQueryBean [menu=" + menu + "]";
	}
}
