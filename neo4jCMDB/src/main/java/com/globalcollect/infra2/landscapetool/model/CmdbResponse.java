/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cvugrine
 */
@XmlRootElement(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmdbResponse {
    
    @XmlElement(name="Time")
    private String time;
    
    @XmlElement(name="Data")
    private CmdbResponseData data;

    
    public String getTime() {
        return time;
    }

    public CmdbResponseData getData() {
        return data;
    }

    public void setData(CmdbResponseData data) {
        this.data = data;
    }
    
    public void setTime(String timeParam) {
        this.time = timeParam;
    }
    
}
