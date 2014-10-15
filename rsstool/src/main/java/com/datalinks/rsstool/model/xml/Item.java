package com.datalinks.rsstool.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Item {

	String title;
	String link;
	String description;

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

	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

}
