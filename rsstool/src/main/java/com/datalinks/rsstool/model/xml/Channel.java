package com.datalinks.rsstool.model.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"title","link","description","image","item"})
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class Channel {

	String title;
	String link;
	String description;
	Image image;
	List<Item> item;

	public List<Item> getItem() {
		return item;
	}

	@XmlElement
	public void setItem(List<Item> items) {
		this.item = items;
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

	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

	public Image getImage() {
		return image;
	}

	@XmlElement
	public void setImage(Image rssImage) {
		this.image = rssImage;
	}

}
