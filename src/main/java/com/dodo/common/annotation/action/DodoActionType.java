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
 */
public enum DodoActionType {
	ALL("","",false,0),
	
	ADD("dodo.common.action.type.ADD","/add.jhtml?clientlang=${clientlanguage}",true,10),
	TOADD("dodo.common.action.type.TOADD","/toadd.jhtml?clientlang=${clientlanguage}",false,11),

	UPDATE("dodo.common.action.type.UPDATE","/update.jhtml?clientlang=${clientlanguage}",true,20),
	TOUPDATE("dodo.common.action.type.TOUPDATE","/toupdate.jhtml?clientlang=${clientlanguage}",false,21),
	
	DELETE("dodo.common.action.type.DELETE","/delete.jhtml?clientlang=${clientlanguage}",true,30),
	
	LIST("dodo.common.action.type.LIST","/list.jhtml?clientlang=${clientlanguage}",true,40),
	VIEW("dodo.common.action.type.VIEW","/view.jhtml?clientlang=${clientlanguage}",false,41),
	TOCHART("dodo.common.action.type.TOCHART","/tochart.jhtml?clientlang=${clientlanguage}",false,42),
	CHARTLIST("dodo.common.action.type.CHARTLIST","/chart_list.jhtml?clientlang=${clientlanguage}",false,43),
	TOPOPUP("dodo.common.action.type.TOPOPUP","/topopup.jhtml?clientlang=${clientlanguage}",false,44),
	POPUPLIST("dodo.common.action.type.POPUPLIST","/popuplist.jhtml?clientlang=${clientlanguage}",false,45),
	
	EXPORT("dodo.common.action.type.EXPORT","",true,50),
	/*
	EXPORTWORD("dodo.common.action.type.EXPORTWORD","/word.jhtml?clientlang=${clientlanguage}",false,51),
	*/
	EXPORTEXCEL("dodo.common.action.type.EXPORTEXCEL","/excel.jhtml?clientlang=${clientlanguage}",false,52),
	EXPORTCHARTEXCEL("dodo.common.action.type.EXPORTCHARTEXCEL","/chart_excel.jhtml?clientlang=${clientlanguage}",false,53),
	
	CHART("dodo.common.action.type.CHART","/chart.jhtml?clientlang=${clientlanguage}",true,60),
	/*
	CHARTEXPORT("dodo.common.action.type.CHARTEXPORT","/chart_export.jhtml?clientlang=${clientlanguage}",false,61),
	*/
	EXCELIMPORT("dodo.common.action.type.EXCELIMPORT","/excel_import.jhtml?clientlang=${clientlanguage}",true,70),
	TOEXCELIMPORT("dodo.common.action.type.TOEXCELIMPORT","/to_excel_import.jhtml?clientlang=${clientlanguage}",false,71),
	EXCELIMPORTTEMPLATE("dodo.common.action.type.EXCELIMPORTTEMPLATE","/excel_import_template.jhtml?clientlang=${clientlanguage}",false,72),
	
	EXCELUPDATE("dodo.common.action.type.EXCELUPDATE","/excel_update.jhtml?clientlang=${clientlanguage}",true,80),
	TOEXCELUPDATE("dodo.common.action.type.TOEXCELUPDATE","/to_excel_update.jhtml?clientlang=${clientlanguage}",false,81),
	EXCELUPDATETEMPLATE("dodo.common.action.type.EXCELUPDATETEMPLATE","/excel_update_template.jhtml?clientlang=${clientlanguage}",false,82)
	;
	private DodoActionType(String name,String link,Boolean isManager,int sortSeq){
		this.name = name;
		this.link = link;
		this.isManager = isManager;
		this.sortSeq = sortSeq;
	}
	private String name;
	private String link;
	private Boolean isManager;
	private int sortSeq;
	public String getName() {
		return name;
	}
	public String getLink() {
		return link;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Boolean getIsManager() {
		return isManager;
	}
	public void setIsManager(Boolean isManager) {
		this.isManager = isManager;
	}
	public int getSortSeq() {
		return sortSeq;
	}
	public void setSortSeq(int sortSeq) {
		this.sortSeq = sortSeq;
	}
}
