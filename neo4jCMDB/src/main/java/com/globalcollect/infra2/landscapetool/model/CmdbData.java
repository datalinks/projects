/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

//  This will act as an utility wrapper around the JAXB elements
public class CmdbData {
    
    private CmdbResponse cmdbResponse;
    private CmdbResponseData cmdbResponseData;
    private CmdbResponseDataChildren children;
    private Set<CmdbResponseDataChildren> dataCenters;
    
    private HashMap<CmdbResponseDataChildren,List<CmdbResponseDataChildren>> dataCenter_SolutionRelation;
    private HashMap<CmdbResponseDataChildren,List<CmdbResponseDataChildren>> solution_ChannelRelation;
    private HashMap<CmdbResponseDataChildren,List<CmdbResponseDataChildren>> channel_EnvironmentRelation;
    private HashMap<CmdbResponseDataChildren,List<CmdbResponseDataChildren>> environment_ApplicationRelation;
    private HashMap<CmdbResponseDataChildren,List<CmdbResponseDataChildren>> application_DbRelation;

    public HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> getApplication_DbRelation() {
        return application_DbRelation;
    }

    public void setApplication_DbRelation(HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> application_DbRelation) {
        this.application_DbRelation = application_DbRelation;
    }

    public HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> getDataCenter_SolutionRelation() {
        return dataCenter_SolutionRelation;
    }

    public void setDataCenter_SolutionRelation(HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> dataCenter_SolutionRelation) {
        this.dataCenter_SolutionRelation = dataCenter_SolutionRelation;
    }

    public HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> getSolution_ChannelRelation() {
        return solution_ChannelRelation;
    }

    public void setSolution_ChannelRelation(HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> solution_ChannelRelation) {
        this.solution_ChannelRelation = solution_ChannelRelation;
    }

    public HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> getChannel_EnvironmentRelation() {
        return channel_EnvironmentRelation;
    }

    public void setChannel_EnvironmentRelation(HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> channel_EnvironmentRelation) {
        this.channel_EnvironmentRelation = channel_EnvironmentRelation;
    }

    public HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> getEnvironment_ApplicationRelation() {
        return environment_ApplicationRelation;
    }

    public void setEnvironment_ApplicationRelation(HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> environment_ApplicationRelation) {
        this.environment_ApplicationRelation = environment_ApplicationRelation;
    }


    
    
    public CmdbResponse getCmdbResponse() {
        return cmdbResponse;
    }

    public void setCmdbResponse(CmdbResponse cmdbResponse) {
        this.cmdbResponse = cmdbResponse;
    }

    public CmdbResponseData getCmdbResponseData() {
        return cmdbResponseData;
    }

    public void setCmdbResponseData(CmdbResponseData cmdbResponseData) {
        this.cmdbResponseData = cmdbResponseData;
    }

    public CmdbResponseDataChildren getChildren() {
        return children;
    }

    public void setChildren(CmdbResponseDataChildren children) {
        this.children = children;
    }

    public Set<CmdbResponseDataChildren> getDataCenters() {
        return dataCenters;
    }

    public void setDataCenters(Set<CmdbResponseDataChildren> dataCenters) {
        this.dataCenters = dataCenters;
    }
    
}
