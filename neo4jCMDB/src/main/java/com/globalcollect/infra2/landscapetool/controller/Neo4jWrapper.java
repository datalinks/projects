/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.controller;

import com.globalcollect.infra2.landscapetool.config.LandscapeVariables;
import com.globalcollect.infra2.landscapetool.model.CmdbData;
import com.globalcollect.infra2.landscapetool.model.CmdbResponseDataChildren;
import com.globalcollect.infra2.landscapetool.model.Neo4jTypes;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

public class Neo4jWrapper implements LandscapeVariables{

    private static final org.slf4j.Logger log  = LoggerFactory.getLogger(Neo4jWrapper.class);
              
    private final SpringRestGraphDatabase springRestGraphDatabase;
            

    
    public Neo4jWrapper (SpringRestGraphDatabase springRestGraphDatabaseParam){
        this.springRestGraphDatabase = springRestGraphDatabaseParam;
    }

    private void modelGcCmdb(CmdbData data)throws Exception{
        
        //  Bossss
        Label label = DynamicLabel.label(GC_NEO4J_LABEL_DATACENTERS);
        Node rootNode = springRestGraphDatabase.createNode(label);
        rootNode.setProperty("id", GC_NEO4J_LABEL_DATACENTERS);
        HashMap<String,Node> createdNodes = new HashMap<String, Node>();
        createdNodes.put(GC_NEO4J_LABEL_DATACENTERS, rootNode);
        
        for(int listIndicator=1; listIndicator<=5 ; listIndicator++){

            log.debug("commencing iteration nr: "+listIndicator);
            HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> mapToWorkWith = getMapToWorkWith(data, listIndicator);
            
            if(mapToWorkWith!=null){
                Set <CmdbResponseDataChildren> keys = mapToWorkWith.keySet();
                    
                for(CmdbResponseDataChildren key : keys){
                    String nameOfKey = key.getLabel();
                    label = DynamicLabel.label(nameOfKey);
                    Node aNode;
                    if(!createdNodes.containsKey(nameOfKey)){
                        aNode = springRestGraphDatabase.createNode(label);
                        aNode.setProperty("id", nameOfKey);
                        createdNodes.put(nameOfKey,aNode);
                    }else{
                        aNode = createdNodes.get(nameOfKey);
                    }

                    CmdbResponseDataChildren rootKey = key.getRootKey();
                    String rootKeyName = rootKey.getLabel();
                    log.debug("Collected the following key: "+key.getLabel()+" ROOT KEy IS "+rootKeyName);
                    //  Getting from hashmap createdNodes
                    rootNode = createdNodes.get(rootKeyName);

                    if(rootNode==null){
                        label = DynamicLabel.label(rootKeyName);
                        rootNode = springRestGraphDatabase.createNode(label);
                        rootNode.setProperty("id",rootKeyName);
                        createdNodes.put(rootKeyName, rootNode);
                    }   
                    rootNode.createRelationshipTo(aNode, getRelationType(listIndicator)); 
                }
            }
        }

    }
    

    private void showWhatYouCollected(CmdbData data){
        for(int listIndicator=1; listIndicator<4 ; listIndicator++){

            HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> mapToWorkWith = getMapToWorkWith(data, listIndicator);
            Set <CmdbResponseDataChildren> keys = mapToWorkWith.keySet();
            
            log.debug("XXXXXX - Iterating over: "+listIndicator+" with relation "+getRelationType(listIndicator));
                    
            for(CmdbResponseDataChildren key : keys){
                
                CmdbResponseDataChildren rootKey = key.getRootKey();
                log.debug("Collected the following key: "+key.getLabel()+" ROOT KEy IS "+rootKey.getLabel());

                
                List<CmdbResponseDataChildren> dataItems  = getListToWorkWith(data, listIndicator, key);
                 if(dataItems!=null){
                    for(CmdbResponseDataChildren dataItem : dataItems){
                           log.debug("Collected the following key: "+key.getLabel()+" with data item "+dataItem.getLabel());
                    }
                 }                
            }
        }
    }

    public void modelGcCmdb(){

        //  Get the latest CMDB data via the cmdbWrapper (does rest call to cmdb)
        CmdbData cmdbData = new CmdbWrapper().getCmdbData();
        
        Transaction tx =springRestGraphDatabase.beginTx();
        try {
                //  This is the root....of all evil..hehe subNodes will be created with other methods
                modelGcCmdb(cmdbData);
                //showWhatYouCollected(cmdbData);
            
                tx.success();
        }catch(Exception ex){
            log.error("Exception during modelling: "+ex.getMessage());
        }finally {
            tx.close();
        }

    }

           
    public void cleanNeo4j(){
        
        String q = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
        String payload = "{\"statements\" : [ { \"statement\" : \""+q+"\"} ]}";
        
        WebResource resource = Client.create().resource( GC_NEO4J_CLEANUP_URL );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
        .type( MediaType.APPLICATION_JSON )
        .entity(payload)
        .post( ClientResponse.class );
        

    }
    
    
    //  Configurable CODE PART...later to be refactored...
    private HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> getMapToWorkWith(CmdbData cmdbData,int listIndicator){
        HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> result = null;
        if(listIndicator == 1){
             result = cmdbData.getDataCenter_SolutionRelation();
        }
        if(listIndicator == 2){
             result = cmdbData.getSolution_ChannelRelation();
        }
        if(listIndicator == 3){
             result = cmdbData.getChannel_EnvironmentRelation();
        }
        if(listIndicator == 4){
             result = cmdbData.getEnvironment_ApplicationRelation();
        }
        if(listIndicator == 5){
             result = cmdbData.getApplication_DbRelation();
        }
        return result;
    
    }
    
    private List <CmdbResponseDataChildren> getListToWorkWith(CmdbData cmdbData,int listIndicator, CmdbResponseDataChildren key){
        List <CmdbResponseDataChildren> result = null;
        HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>> map;
        
        if(listIndicator == 1){
             map = cmdbData.getDataCenter_SolutionRelation();
             result = map.get(key);
        }
        if(listIndicator == 2){
             map = cmdbData.getSolution_ChannelRelation();
             result = map.get(key);
        }
        if(listIndicator == 3){
             map = cmdbData.getChannel_EnvironmentRelation();
             result = map.get(key);
        }
        if(listIndicator == 4){
             map = cmdbData.getEnvironment_ApplicationRelation();
             result = map.get(key);
        }
        if(listIndicator == 5){
             map = cmdbData.getApplication_DbRelation();
             result = map.get(key);
        }

        return result;
    }
    
    private Neo4jTypes.GcCMDBRelTypes getRelationType(int listIndicator){
        Neo4jTypes.GcCMDBRelTypes result = null;
        if(listIndicator == 1)
            result = Neo4jTypes.GcCMDBRelTypes.PART_OF;
        if(listIndicator == 2)
            result = Neo4jTypes.GcCMDBRelTypes.PROVIDES_SOLUTION;
        if(listIndicator == 3)
            result = Neo4jTypes.GcCMDBRelTypes.HAS_CHANNEL;
        if(listIndicator == 4)
            result = Neo4jTypes.GcCMDBRelTypes.IN_ENVIRONMENT;
        if(listIndicator == 5)
            result = Neo4jTypes.GcCMDBRelTypes.HOSTS_APP;
        return result;
    }


    
}
