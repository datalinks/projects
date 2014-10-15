package com.datalinks.restwsexample.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductXML {

	private String name;
	private String category;
	private int price;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
		
}

