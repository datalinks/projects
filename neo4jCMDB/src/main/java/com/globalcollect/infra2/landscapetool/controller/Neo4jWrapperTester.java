/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globalcollect.infra2.landscapetool.controller;

import com.globalcollect.infra2.landscapetool.config.LandscapeVariables;
import com.globalcollect.infra2.landscapetool.model.Neo4jTypes;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

public class Neo4jWrapperTester implements LandscapeVariables{

    private static final org.slf4j.Logger log  = LoggerFactory.getLogger(Neo4jWrapperTester.class);
              
    private final SpringRestGraphDatabase springRestGraphDatabase;
            

    
    public Neo4jWrapperTester (SpringRestGraphDatabase springRestGraphDatabaseParam){
        this.springRestGraphDatabase = springRestGraphDatabaseParam;
    }

    public void generateTestData(){
        Transaction tx =springRestGraphDatabase.beginTx();
        try {
            
            Label myLabel = DynamicLabel.label("FAMILIE");
            Node familieNode = springRestGraphDatabase.createNode(myLabel);
            
            myLabel = DynamicLabel.label("CHRIS");
            Node chrisNode = springRestGraphDatabase.createNode(myLabel);
            chrisNode.setProperty("name", "chris");
            chrisNode.setProperty("leeftijd", 41);
            
            myLabel = DynamicLabel.label("PUCK");
            Node puckNode = springRestGraphDatabase.createNode(myLabel);
            puckNode.setProperty("name", "puck");    
            
            myLabel = DynamicLabel.label("BO");
            Node boNode = springRestGraphDatabase.createNode(myLabel);
            boNode.setProperty("name", "bo");    
   
            myLabel = DynamicLabel.label("LOTTE");
            Node lotteNode = springRestGraphDatabase.createNode(myLabel);
            lotteNode.setProperty("name", "lotte");    

            familieNode.createRelationshipTo(chrisNode, Neo4jTypes.RelTypes.FAMILIE_VAN);
            familieNode.createRelationshipTo(lotteNode, Neo4jTypes.RelTypes.FAMILIE_VAN);
            familieNode.createRelationshipTo(puckNode, Neo4jTypes.RelTypes.FAMILIE_VAN);
            familieNode.createRelationshipTo(boNode, Neo4jTypes.RelTypes.FAMILIE_VAN);
            
            puckNode.createRelationshipTo(chrisNode, Neo4jTypes.RelTypes.DOCHTER_VAN);
            boNode.createRelationshipTo(chrisNode, Neo4jTypes.RelTypes.DOCHTER_VAN);
            lotteNode.createRelationshipTo(chrisNode, Neo4jTypes.RelTypes.VROUW_VAN);
            
   	    tx.success();
        }finally {
            tx.close();
        }

    }

    
}
