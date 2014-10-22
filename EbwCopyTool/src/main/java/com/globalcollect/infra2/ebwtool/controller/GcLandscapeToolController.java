package com.globalcollect.infra2.ebwtool.controller;

import com.globalcollect.infra2.ebwtool.config.Variables;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
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
public class GcLandscapeToolController implements Variables {

/*    @Bean
    SpringRestGraphDatabase springRestGraphDatabase() {
        return new SpringRestGraphDatabase(GC_NEO4J_DB_URL);
    }
*/
    @Autowired(required = true)
    SpringRestGraphDatabase springRestGraphDatabase;

    @RequestMapping("/")
    public String showIndex(String name, Model model) {
        model.addAttribute("name", name);
        return "neo4joptionsPage";
    }



}
