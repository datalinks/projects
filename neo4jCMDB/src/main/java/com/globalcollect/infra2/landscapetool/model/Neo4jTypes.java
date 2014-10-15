/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.model;

import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author cvugrine
 */
public class Neo4jTypes {
    
    public static enum RelTypes implements RelationshipType {
        IS_EEN,
        DOCHTER_VAN,
        VROUW_VAN,
        FAMILIE_VAN
    }
    
    public static enum GcCMDBRelTypes implements RelationshipType {
        PART_OF,
        HAS_CHANNEL,
        PROVIDES_SOLUTION,
        IN_LOCATION,
        IN_ENVIRONMENT,
        HOSTS_APP,
        USES_DB
    }

    public static enum NodeTypes {
        DATACENTER,
        SOLUTION,
        CHANNEL,
        ENVIRONMENT,
        APP,
        DB
    }

    
}
