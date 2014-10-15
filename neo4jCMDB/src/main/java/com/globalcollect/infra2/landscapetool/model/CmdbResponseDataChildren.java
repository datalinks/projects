/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.model;

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Childeren")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmdbResponseDataChildren {
 
    @XmlElement(name="Children")
    private Set<CmdbResponseDataChildren> children;

    @XmlElement(name="Id")
    private String id;

    private CmdbResponseDataChildren rootKey;

    public CmdbResponseDataChildren getRootKey() {
        return rootKey;
    }

    public void setRootKey(CmdbResponseDataChildren rootKey) {
        this.rootKey = rootKey;
    }
    
    public Set<CmdbResponseDataChildren> getChildren() {
        return children;
    }

    public void setChildren(Set<CmdbResponseDataChildren> children) {
        this.children = children;
    }
    
    @XmlElement(name="Uuid")
    private String uuid;
    
    @XmlElement(name="UuidAlias")
    private String uuidAlias;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuidAlias() {
        return uuidAlias;
    }

    public void setUuidAlias(String uuidAlias) {
        this.uuidAlias = uuidAlias;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    @XmlElement(name="ModelId")
    private String modelId;

    @XmlElement(name="Label")
    private String label;



}
