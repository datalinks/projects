package com.datalinks.rsstool.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class Rss {

	String version;
	Channel channel;
	
	public String getVersion() {
		return version;
	}

	
	@XmlAttribute
	public void setVersion(String version) {
		this.version = version;
	}

	public Channel getChannel() {
		return channel;
	}

	@XmlElement
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
