package com.datalinks.rsstool.model;

import java.util.List;

import com.datalinks.rsstool.model.xml.Channel;
import com.datalinks.rsstool.model.xml.Item;
import com.datalinks.rsstool.model.xml.Rss;

public class RssFileModel {

	private List<RssFile> rssFilez;
	private RssFile selectedRssFile;
	private Rss selectedRssXmlFile;
	private boolean doDelete;
	private String fileName;
	private List<Item> rssItems;
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<Item> getRssItems() {
		return rssItems;
	}

	public void setRssItems(List<Item> rssItems) {
		this.rssItems = rssItems;
	}

	public Rss getSelectedRssXmlFile() {
		return selectedRssXmlFile;
	}

	public void setSelectedRssXmlFile(Rss selectedRssXmlFile) {
		this.selectedRssXmlFile = selectedRssXmlFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public RssFile getSelectedRssFile() {
		return selectedRssFile;
	}

	public void setSelectedRssFile(RssFile selectedRssFile) {
		this.selectedRssFile = selectedRssFile;
	}

	public boolean isDoDelete() {
		return doDelete;
	}

	public void setDoDelete(boolean doDelete) {
		this.doDelete = doDelete;
	}

	public List<RssFile> getRssFilez() {
		return rssFilez;
	}

	public void setRssFilez(List<RssFile> rssFilez) {
		this.rssFilez = rssFilez;
	}

}
