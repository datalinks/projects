package com.globalcollect.infra2.landscapetool.controller;

import com.globalcollect.infra2.landscapetool.config.LandscapeVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ComponentScan("com.globalcollect.infra2.landscapetool.controller")
public class GcLandscapeToolController implements LandscapeVariables { 

                
    @Bean
    SpringRestGraphDatabase springRestGraphDatabase(){
        return new SpringRestGraphDatabase(GC_NEO4J_DB_URL);
    }

    @Autowired(required=true)
    SpringRestGraphDatabase springRestGraphDatabase;

    
    @RequestMapping("/")
    public String showIndex(String name, Model model) {
        model.addAttribute("name", name);
        return "neo4joptionsPage";
    }

    @RequestMapping("/createCmdb")
    public String createEnv(@RequestParam(value="name", required=false, defaultValue="dev") String name, Model model) {
        model.addAttribute("name", name);
        new Neo4jWrapper(springRestGraphDatabase).modelGcCmdb();
        return "neo4joptionsPage";
    }

    @RequestMapping("/cleanNeo4j")
    public String cleanNeo4j( String name, Model model) {
        model.addAttribute("name", name);
        new Neo4jWrapper(springRestGraphDatabase).cleanNeo4j();
        return "neo4joptionsPage";
    }
    
}
