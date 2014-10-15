package com.datalinks.rsstool.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Image {

	String url;
	String title;
	String link;

	public String getUrl() {
		return url;
	}

	@XmlElement
	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	@XmlElement
	public void setLink(String link) {
		this.link = link;
	}

}
