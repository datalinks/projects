/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.config;


/**
 *
 * @author cvugrine
 */
public interface LandscapeVariables {
    public String NEO4J_DB_PATH = "/data/neo4j";
    static String DB_NAME = "/opt/neo4j/data/graph.db";
    public String nodestore_mapped_memory_size = "10M";
    public String string_block_size = "60";
    public String array_block_size = "300";
    
    //  neo4j url
    public String GC_NEO4J_DB_URL = "http://localhost:7474/db/data/";
    public String GC_NEO4J_CLEANUP_URL = "http://localhost:7474/db/data/transaction/commit";
    public String GC_NEO4J_LABEL_DATACENTERS = "GC_DATACENTERS";
    
    
    //  cmdb url for omgevingen
    public String GC_CMDB_DATACENTRES_URL = "http://cmdb.globalcollect.nl/api/xml/resource/9047?properties=1&links=1&descendants=1";

}
