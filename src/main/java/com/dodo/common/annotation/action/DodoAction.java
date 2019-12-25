package com.dodo.common.annotation.action;

/**
 * action 层的业务类型
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 * 
 */
public enum DodoAction {
	ALL(new DodoActionType[]{DodoActionType.ALL}),
	ADD(new DodoActionType[]{DodoActionType.TOADD,DodoActionType.ADD}),
	UPDATE(new DodoActionType[]{DodoActionType.TOUPDATE,DodoActionType.UPDATE}),
	DELETE(new DodoActionType[]{DodoActionType.DELETE}),
	VIEW(new DodoActionType[]{DodoActionType.LIST,DodoActionType.VIEW,DodoActionType.TOCHART,DodoActionType.CHARTLIST,DodoActionType.TOPOPUP,DodoActionType.POPUPLIST}),
	EXPORT(new DodoActionType[]{DodoActionType.EXPORT,/*DodoActionType.EXPORTWORD,*/DodoActionType.EXPORTEXCEL,DodoActionType.EXPORTCHARTEXCEL}),
	CHART(new DodoActionType[]{DodoActionType.CHART/*,DodoActionType.CHARTEXPORT*/}),
	EXCELIMPORT(new DodoActionType[]{DodoActionType.TOEXCELIMPORT,DodoActionType.EXCELIMPORTTEMPLATE,DodoActionType.EXCELIMPORT}),
	EXCELUPDATE(new DodoActionType[]{DodoActionType.TOEXCELUPDATE,DodoActionType.EXCELUPDATETEMPLATE,DodoActionType.EXCELUPDATE});
	DodoActionType[] actionTypes;
	
	private DodoAction(DodoActionType[] actionTypes){
		this.actionTypes = actionTypes;
	}
	
	public DodoActionType[] getActionTypes() {
		return actionTypes;
	}
}
