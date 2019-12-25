package com.dodo.weixin.bean.qrcode;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class QrCodeCreateAction {
	private Map<String,Integer> scene = new HashMap<String, Integer>();

	public Map<String, Integer> getScene() {
		return scene;
	}

	public void setScene(Map<String, Integer> scene) {
		this.scene = scene;
	}

	public QrCodeCreateAction(Integer scene_id) {
		this.scene.put("scene_id", scene_id);
	}

	@Override
	public String toString() {
		return "QrCodeCreateAction [scene=" + scene + "]";
	}
}
