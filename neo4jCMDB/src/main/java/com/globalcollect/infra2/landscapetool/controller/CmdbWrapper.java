/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.controller;

import com.globalcollect.infra2.landscapetool.config.LandscapeVariables;
import static com.globalcollect.infra2.landscapetool.config.LandscapeVariables.GC_NEO4J_CLEANUP_URL;
import com.globalcollect.infra2.landscapetool.model.CmdbData;
import com.globalcollect.infra2.landscapetool.model.CmdbResponse;
import com.globalcollect.infra2.landscapetool.model.CmdbResponseData;
import com.globalcollect.infra2.landscapetool.model.CmdbResponseDataChildren;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cvugrine
 */
public class CmdbWrapper implements LandscapeVariables{

    private static final Logger log  = LoggerFactory.getLogger(CmdbWrapper.class);
    private JAXBContext jaxbContext = null;
    private Unmarshaller unmarshaller = null;
    
    
    public static void main(String[] args){
        CmdbWrapper cm = new CmdbWrapper();
        cm.getCmdbData();
    }

    
    public CmdbWrapper(){
        //  JAXB Unmarshal the response (Meaning Only XML response allowed)
        try {
            jaxbContext = JAXBContext.newInstance(CmdbResponse.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            log.error("JAXBException creating unmarshalling object:  "+ex.getMessage());
        }
    }

    private StringReader getFileContent(){
        StringReader result = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("/Users/Chris/Documents/workspaces/netbeans/GcLandscapeTool/src/main/resources/9047.xml"));
            String tmpString = new String(encoded,"UTF-8");
            result = new StringReader(tmpString);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CmdbWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private StringReader doRestCall(){
        
        WebResource resource = Client.create().resource( GC_CMDB_DATACENTRES_URL );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
        .type( MediaType.APPLICATION_JSON )
        .get( ClientResponse.class );
        
        log.debug("executing: "+GC_CMDB_DATACENTRES_URL);
                
        //  Only parse data if HTTP responseCode = 200
        if(response.getStatus()==200){
            
            if(log.isDebugEnabled()){           
                MultivaluedMap<String, String> responseHeaders =  response.getHeaders();
                Set<String> keys = responseHeaders.keySet();
                for(String key : keys){
                    log.debug("result... "+key+" contains value: "+responseHeaders.get(key));
                }
            }
           
            String output = response.getEntity(String.class);
            StringReader reader = new StringReader(output);
            return reader;
        }else{
            log.error("Error in getting result from: "+GC_CMDB_DATACENTRES_URL+" status was: "+response.getStatus());
        }
        return null;
    }
    
    public CmdbData getCmdbData(){
        CmdbData result = new CmdbData();   
        

        //  Doin REST CALL
//        StringReader reader = doRestCall();
        StringReader reader = getFileContent();
        
        HashMap<CmdbResponseDataChildren,List <CmdbResponseDataChildren>> dataCenter_SolutionRelation = new HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>>();
        HashMap<CmdbResponseDataChildren,List <CmdbResponseDataChildren>> solution_ChannelRelation = new HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>>();
        HashMap<CmdbResponseDataChildren,List <CmdbResponseDataChildren>> channel_EnvironmentRelation = new HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>>();
        HashMap<CmdbResponseDataChildren,List <CmdbResponseDataChildren>> environment_AppRelations = new HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>>();
        HashMap<CmdbResponseDataChildren,List <CmdbResponseDataChildren>> app_DbRelations = new HashMap<CmdbResponseDataChildren, List<CmdbResponseDataChildren>>();

        //  Parsing XML 
        //  unmarshaller should not be null in this phase (after doing doRestall or other method)    
        if(unmarshaller!=null){
            try {
                CmdbResponse cmdbRes = (CmdbResponse)unmarshaller.unmarshal(reader);
                result.setCmdbResponse(cmdbRes);
                CmdbResponseData cmdbResData = cmdbRes.getData();                    
                result.setCmdbResponseData(cmdbResData);
                CmdbResponseDataChildren cmdResDataChild = cmdbResData.getCmdbResponseDataChildren();
                result.setChildren(cmdResDataChild);

                Set<CmdbResponseDataChildren> dataCenters =  cmdResDataChild.getChildren();
                result.setDataCenters(dataCenters);


                //  Looping Datacenters
                for(CmdbResponseDataChildren dataCenter : dataCenters ){
                    
                    
                    //  Per Datacenter do the following
                     log.debug("PARSING DC: "+dataCenter.getLabel());
                     populateHashMap( null, dataCenter, dataCenter_SolutionRelation );
                     List<CmdbResponseDataChildren> solutions =  dataCenter_SolutionRelation.get(dataCenter);
                     
                    
                    for(CmdbResponseDataChildren solution : solutions ){
                        populateHashMap( dataCenter, solution, solution_ChannelRelation );
                        List<CmdbResponseDataChildren> channels =  solution_ChannelRelation.get(solution);
                        if(channels!=null && channels.size()>0){

                            for(CmdbResponseDataChildren channel : channels ){
                                populateHashMap( solution, channel, channel_EnvironmentRelation );
                                List<CmdbResponseDataChildren> environments =  channel_EnvironmentRelation.get(channel);
                                if(environments!=null && environments.size()>0){

                                    for(CmdbResponseDataChildren environment : environments ){
                                        
                                        log.debug("----- ENVIRONMENT: "+environment.getLabel());
                                        populateHashMap( channel, environment, environment_AppRelations );
                                        List<CmdbResponseDataChildren> apps =  environment_AppRelations.get(environment);
                                        if(apps!=null && apps.size()>0){

                                            log.debug("----- ENVIRONMENT: "+environment.getLabel()+" apps size: "+apps.size());
                                            for(CmdbResponseDataChildren app : apps ){

                                                log.debug("----- APP: "+app.getLabel());
                                                populateHashMap( environment, app, app_DbRelations );
                                            }                                                         
                                        }

                                        
                                    }                                                         
                                }
                            }
                        }
                    }                         
                }
                //  EOF: Looping Datacenters
                if(dataCenter_SolutionRelation.size()>0)
                    result.setDataCenter_SolutionRelation(dataCenter_SolutionRelation);   
                if(solution_ChannelRelation.size()>0)
                    result.setSolution_ChannelRelation(solution_ChannelRelation);
                if(channel_EnvironmentRelation.size()>0)
                    result.setChannel_EnvironmentRelation(channel_EnvironmentRelation);                    
                if(environment_AppRelations.size()>0)
                    result.setEnvironment_ApplicationRelation(environment_AppRelations);
                if(app_DbRelations.size()>0)
                    result.setApplication_DbRelation(app_DbRelations);                   
            } catch (JAXBException ex) {
                log.error("JAXBException in unmarshalling:  "+ex.getMessage());
            }
        }else{
            log.error("Unmarshller == NULL!!!!!");
        }
        return result;
    }
        
    private void populateHashMap( CmdbResponseDataChildren rootKey, CmdbResponseDataChildren key, HashMap<CmdbResponseDataChildren,List <CmdbResponseDataChildren>> map ){

        //  Only for the root
        if(rootKey==null){
            CmdbResponseDataChildren tmpKey = new CmdbResponseDataChildren();
            tmpKey.setLabel(GC_NEO4J_LABEL_DATACENTERS);
            rootKey = tmpKey;
        }
            
        
        log.debug("populate hashMap KEY: "+key.getLabel()+" with ROOTKEY: "+rootKey.getLabel());                        
        List<CmdbResponseDataChildren> solz = null;
        Set<CmdbResponseDataChildren> tmpDbs = key.getChildren();
        key.setRootKey(rootKey);
        
        if(tmpDbs!=null){
            solz = new ArrayList<CmdbResponseDataChildren>();
            for(CmdbResponseDataChildren tmpDb : tmpDbs){
                Set<CmdbResponseDataChildren> tmpDbz = tmpDb.getChildren();
                for(CmdbResponseDataChildren tmpDb2 : tmpDbz){
                    if(tmpDb2 instanceof CmdbResponseDataChildren){
                        solz.add(tmpDb2);
                        log.debug("Adding the following to arrayList for... "+key.getLabel()+" data: "+tmpDb2.getLabel());                        
                    }
                }
                if(solz.size()>0)
                   map.put(key, solz);                        
            }
        }else{
            log.debug("tmpDBs == null, in populateHashMap....when populating: "+key.getLabel());
            map.put(key, solz);
        }
    }
}
