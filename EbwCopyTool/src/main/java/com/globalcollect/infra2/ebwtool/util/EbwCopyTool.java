/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalcollect.infra2.ebwtool.util;

import com.globalcollect.infra2.ebwtool.config.Variables;
import com.globalcollect.infra2.ebwtool.model.Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author cvugrinec
 */
public class EbwCopyTool extends Thread implements Variables {

    static Logger log = Logger.getLogger(EbwCopyTool.class.getName());
    private static List<Server> serverList = new ArrayList<Server>();
    private static String sequence;
    private static String EBWT_SEQ_FILE;
    private static String EBWT_SRC_DIR;
    private static String NEXT_SEQUENCE;

    

    //  Initializing...app need to have servers.properties 
    //  this can be changed to other path as well, just change the serverFilename var
    //  format should be [someServerName]=[ipaddress],[directory], [location of Servers.properties file]
    private void init() throws IOException, Exception{
        Properties serverPropz = new Properties();
        Properties appPropz = new Properties();

	InputStream input = null;
        Writer writer = null;
	try {
                // Loading the app.properties file
		input = getClass().getClassLoader().getResourceAsStream(EBWT_APP_CFG);
		if (input == null) {
                    log.error("EbwCopyTool could not initialize the "+EBWT_APP_CFG+" file");
                    throw new Exception();
                }
		appPropz.load(input);
            
                // Loading the server.properties file
		input = getClass().getClassLoader().getResourceAsStream(EBWT_SERVER_CFG);
		if (input == null) {
                    log.error("EbwCopyTool could not initialize the "+EBWT_SERVER_CFG+" file");
                    throw new Exception();
                }
		serverPropz.load(input);
 
		Enumeration<?> e = serverPropz.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String[] values = serverPropz.getProperty(key).split(",");
                        Server aServer = new Server();
                        aServer.setName(key);
                        String ipAddress = values[0];
                        String dataDirectory = values[1];
                        String propertyFile = values[2];
                        aServer.setIpAddress(ipAddress);
                        aServer.setDataDirectory(dataDirectory);
                        aServer.setPropertyFile(propertyFile);
                        if(ipAddress==null || dataDirectory==null || propertyFile==null  ){
                            throw new Exception("properties not correct filled  for...key: "+key);
                        }
                        serverList.add(aServer);
                        
		}         
                // ;)
                EBWT_SRC_DIR = appPropz.getProperty("EBWT_SRC_DIR");
                EBWT_SEQ_FILE = appPropz.getProperty("EBWT_SEQ_FILE");

                BufferedReader br = new BufferedReader(new FileReader(EBWT_SEQ_FILE));
                String  sCurrentLine = null;
		while ((sCurrentLine = br.readLine()) != null) {
                    sequence = sCurrentLine;
		}
                NEXT_SEQUENCE = StringUtils.replaceChars(sequence, "abcdefghijkl","bcdefghijkla");
                //  write nextSequence into sequence file
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(EBWT_SEQ_FILE), "utf-8"));
                writer.write(NEXT_SEQUENCE);
                log.info("Setting todays("+EbwCopyToolHelper.getCurrentDateTime()+") sequence to: "+NEXT_SEQUENCE);
                
        }catch(IOException iox){
            String msg = "EbwCopyTool could not initialize the "+EBWT_SERVER_CFG+" file or "+EBWT_APP_CFG+" file, ioexception msg : "+iox.getMessage();
            log.error(msg);
            throw new Exception(msg);
        }catch(Exception ex){
            String msg = "EbwCopyTool could not be initialized .... msg is: "+ex.getMessage();
            log.error(msg);
            throw new Exception(msg);
        }finally{
            writer.close();
            input.close();
        }
    }
    
    
    /*  Copy file remote to local
        Change the sequence property
        Copy file local to remote 
        Cleanup the file
    */
    private void changeRemotePropertyFile(Server server) throws IOException, InterruptedException, Exception{
        String host = server.getIpAddress();
        String remotePropFileName = server.getPropertyFile();
        
        String msg;
        
        // Copy File remote to local
        String[] cmds = new String[]{"scp",EBWT_USER+"@"+host+":"+remotePropFileName,"." };
        ProcessBuilder pb = new ProcessBuilder(cmds);
        Process p = pb.start();
        if(p.waitFor()!=0){
            msg = "Error executing cmd: "+Arrays.toString(cmds);
            log.error(msg);
            throw new IOException(msg);
        }
        // Change the sequence property
        Properties prop = new Properties();
        String[] choppedString = StringUtils.split(remotePropFileName, "/");
        String fileName = choppedString[choppedString.length-1];
        InputStream input = null;
        FileOutputStream out = null;
	try{
            input = new FileInputStream(fileName);
            prop.load(input);
            log.debug("REMOTE SEQUENCE PROPERTY WAS: "+prop.getProperty("eiger.tableset")+" NOW SET TO: "+NEXT_SEQUENCE+" determined filename: "+fileName);
            out = new FileOutputStream(fileName);
            prop.setProperty("eiger.tableset", NEXT_SEQUENCE);
            prop.store(out, null);
        }catch(Exception ex){
            msg = "Exception during setting property to : "+fileName;
            log.error(msg);
            throw new IOException(msg);
        }finally{
            input.close();  
            out.close();
        }

        // Copy Local File to remote
        cmds = new String[]{"scp",fileName,EBWT_USER+"@"+host+":"+remotePropFileName };
        pb = new ProcessBuilder(cmds);
        //p = pb.start();
        if(p.waitFor()!=0){
            msg = "Error executing cmd: "+Arrays.toString(cmds);
            log.error(msg);
            throw new Exception(msg);
        }
        
        // Remove local file
        cmds = new String[]{"rm","-f",fileName };
        pb = new ProcessBuilder(cmds);
        p = pb.start();
        if(p.waitFor()!=0){
            msg = "Error executing cmd: "+Arrays.toString(cmds);
            log.error(msg);
            throw new Exception(msg);
        }

    }
    
    
    private boolean renameFiles() throws Exception{
    	Path dir = Paths.get(EBWT_SRC_DIR);
        boolean result = true;
        String msg;
        
        DirectoryStream<Path> stream = null;
        try{
            stream = Files.newDirectoryStream(dir, "*.tbl");
        }catch(IOException iox){
            msg = "Could not open Directory for reading SRC DATA files: "+EBWT_SRC_DIR+" msg was: "+iox.getMessage();
            log.error(msg);
            throw new Exception(msg);
        }

        for (Path file : stream) {
            File oldfile = file.toFile();
            String fileName = oldfile.getName();
            
            BufferedWriter writer = null;
            try{
                //  Write next sequence to sequenceFile
                writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(EBWT_SEQ_FILE), "utf-8"));
                writer.write(NEXT_SEQUENCE);
                writer.close();
            }catch(IOException iox){
                msg = "Could not open Required secuence file: "+EBWT_SEQ_FILE+" msg was: "+iox.getMessage();
                log.error(msg);
                throw new Exception(msg);
            }finally{
                try {
                    writer.close();
                } catch (IOException ex) {
                    throw new Exception("could not close writer!!! "+ex.getMessage());
                }
            }
            
            //  Renaming of filename
            String newFileName = EbwCopyToolHelper.removeLastChar(fileName.replace(".tbl", ""));
            newFileName=newFileName+NEXT_SEQUENCE+".tbl";
            File newfile =new File(EBWT_SRC_DIR+newFileName);
            if(oldfile.renameTo(newfile)){
                log.info("Rename from : "+oldfile.getName()+" to : "+newFileName+" SUCCES");
            }else{
                log.error("Rename from : "+oldfile.getName()+" to : "+newFileName+" FAILED");
                result = false;
            }
        }
        return result;
    }
    
    /*
        Does the actual Rsyncing per server
    */
    private void rsyncFiles(Server server) throws InterruptedException, IOException, Exception{
        String host = server.getIpAddress();
        String destinationDirectory = server.getDataDirectory();
        String propertyFile = server.getPropertyFile();
        
        String[] cmds = new String[]{"rsync", "-avcz",  EBWT_SRC_DIR, EBWT_USER + "@" + host + ":" + destinationDirectory};
        StringBuilder sb = new StringBuilder();
        for (String cmd : cmds) {
            sb.append(cmd);
        }
        
        ProcessBuilder pb = new ProcessBuilder(cmds);
        Process p = pb.start();
        int val = p.waitFor();
        if (val != 0) {
            log.error("RSYNCING FAILED, val : "+val+" on host: "+host+" with command: "+sb.toString());
        }else{
            log.info("RSYNCING SUCCESS, val : "+val+" on host: "+host+" with command: "+sb.toString());
        }        
    }
    
    public void run() {

        String host = null;
        try {
            for(Server srv : serverList){
                //  Rsync files
                rsyncFiles(srv);
                //  After Syncing the remote property file should be changed
                changeRemotePropertyFile(srv);
                //  After changing the remote propertyFile, the Tables should be loaded

                //  After Loading the tables the server instance should be restarted, Please also make check if server can be STARTED!!!
            }
        } catch (InterruptedException ex) {
            log.error("InteruptedException handling of host: "+host+" exception message was: "+ex.getMessage());
            System.exit(1);            
        } catch (IOException ex) {
            log.error("IOException during handling of host: "+host+" exception message was: "+ex.getMessage());
            System.exit(1);            
        } catch (Exception ex) {
            log.error("General Exception during handling of host: "+host+" exception message was: "+ex.getMessage());
            System.exit(1);            
        } finally{
            log.info("==================   EbwCopyTool STOPPED at: "+EbwCopyToolHelper.getCurrentDateTime()+"   ==================");
        }
    }
    
    public static void main(String[] args) {

        EbwCopyTool ebw = new EbwCopyTool();
        log.info("================   EbwCopyTool STARTED : "+EbwCopyToolHelper.getCurrentDateTime()+"   ================"); 
        try{
            ebw.init();        
            log.info("Start rename and copy: "+EbwCopyToolHelper.getCurrentDateTime()+" to "+serverList.size()+" servers");  
            ebw.renameFiles();
            ebw.start();
        }catch(Exception ex){
            log.error("Something went wrong initializing...,msg: "+ex.getMessage());
            System.exit(1);
        }        
    }
}
