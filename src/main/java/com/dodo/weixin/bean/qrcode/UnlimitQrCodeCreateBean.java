package com.dodo.weixin.bean.qrcode;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class UnlimitQrCodeCreateBean {
	private String action_name;
	private QrCodeCreateAction action_info;
	public String getAction_name() {
		return action_name;
	}
	public QrCodeCreateAction getAction_info() {
		return action_info;
	}
	public UnlimitQrCodeCreateBean(Integer scene_id) {
		super();
		this.action_name = "QR_LIMIT_SCENE";
		this.action_info = new QrCodeCreateAction(scene_id);
	}
}
