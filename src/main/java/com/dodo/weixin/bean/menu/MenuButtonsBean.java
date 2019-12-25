package com.dodo.weixin.bean.menu;

import java.util.ArrayList;
import java.util.List;

import com.dodo.weixin.exception.WeixinException;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MenuButtonsBean {

	/**
	 *  一级菜单 最多3个
	 */
	public List<MenuBean> button = new ArrayList<MenuBean>(3);

	public void addMenu(MenuBean menu) throws WeixinException {
		if(button.size() < 3){
			button.add(menu);
		}else{
			throw new WeixinException("sub menu max count 3");
		}
	}

	@Override
	public String toString() {
		return "MenuButtonsBean [button=" + button + "]";
	}
}
