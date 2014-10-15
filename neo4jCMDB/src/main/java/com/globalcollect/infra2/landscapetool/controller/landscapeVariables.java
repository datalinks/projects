/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.controller;


/**
 *
 * @author cvugrine
 */
public interface landscapeVariables {
    public String NEO4J_DB_PATH = "/data/neo4j";
    static String DB_NAME = "/opt/neo4j/data/graph.db";
    public String nodestore_mapped_memory_size = "10M";
    public String string_block_size = "60";
    public String array_block_size = "300";
    
}
