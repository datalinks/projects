package com.datalinks.infoboard;

public class InfoBoardServiceBean {

	private static volatile InfoBoardServiceBean instance = new InfoBoardServiceBean();
	
	private String site;

	private InfoBoardServiceBean() {
	}
	
	public static InfoBoardServiceBean getInstance(){
		return instance;
	}
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
}
