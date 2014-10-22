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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author cvugrine
 */
public class EbwCopyTool extends Thread implements Variables {

    static Logger log = Logger.getLogger(EbwCopyTool.class.getName());
    private static List<Server> serverList = new ArrayList<Server>();
    private static String sequence;

    //  Initializing...app need to have servers.properties in classpath, 
    //  this can be changed to other path as well, just change the serverFilename var
    //  format should be [someServerName]=[ipaddress],[directory]
    private void init(){
        Properties prop = new Properties();
	InputStream input = null;
        String serverFilename = "servers.properties";
	try {
 
		input = getClass().getClassLoader().getResourceAsStream(serverFilename);
		if (input == null) {
                    log.error("EbwCopyTool could not initialize the "+serverFilename+" file");
                    throw new Exception();
                }
		prop.load(input);
 
		Enumeration<?> e = prop.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String[] values = prop.getProperty(key).split(",");
                        Server aServer = new Server();
                        aServer.setName(key);
                        aServer.setIpAddress(values[0]);
                        aServer.setDataDirectory(values[1]);
                        serverList.add(aServer);
		}              
                BufferedReader br = new BufferedReader(new FileReader(EBWT_SEQ_FILE));
                String  sCurrentLine = null;
		while ((sCurrentLine = br.readLine()) != null) {
                    sequence = sCurrentLine;
		}
                log.info("Setting todays("+getCurrentDateTime()+") sequence to: "+sequence);
                
        }catch(IOException iox){
            log.error("EbwCopyTool could not initialize the "+serverFilename+" file or "+EBWT_SEQ_FILE+" file, ioexception msg : "+iox.getMessage());
        }catch(Exception ex){
            log.error("EbwCopyTool could not be initialized ,plz doublecheck your prop file ("+serverFilename+") ,msg is: "+ex.getMessage() );
            System.exit(1);
        }
    }
    
    public static void main(String[] args) throws IOException, Exception {

        EbwCopyTool ebw = new EbwCopyTool();
        ebw.init();
        ebw.renameFiles();
        log.info("EbwCopyTool STARTED at: "+ebw.getCurrentDateTime()+" copy to "+serverList.size()+" servers");        
        ebw.start();
    }

    public void run() {

        String host = null;
        try {
            for(Server srv : serverList){
                rsyncFiles(srv.getIpAddress(),srv.getDataDirectory());
            }
        } catch (InterruptedException ex) {
            log.error("InteruptedException during rsync for host: "+host+" exception message was: "+ex.getMessage());
        } catch (IOException ex) {
            log.error("IOException during rsync for host: "+host+" exception message was: "+ex.getMessage());
        } finally{
            log.info("EbwCopyTool STOPPED at: "+getCurrentDateTime());
        }

    }

    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    
    
    private void renameFiles() throws IOException, Exception{
    	Path dir = Paths.get(EBWT_SRC_DIR);

        DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.tbl");
        for (Path file : stream) {
            File oldfile = file.toFile();
            String fileName = oldfile.getName();
            String nextSequence = StringUtils.replaceChars(sequence, "abcdefghijkl","bcdefghijkla");
            
            //  Write next sequence to sequenceFile
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(EBWT_SEQ_FILE), "utf-8"));
            writer.write(nextSequence);
            writer.close();
            
            String newFileName = fileName.replace(".tbl", nextSequence+".tbl");
            File newfile =new File(EBWT_SRC_DIR+newFileName);
            if(oldfile.renameTo(newfile)){
                log.error("Rename from : "+oldfile.getName()+" to : "+newFileName+" SUCCES");
            }else{
                log.error("Rename from : "+oldfile.getName()+" to : "+newFileName+" FAILED");
            }
        }
        

    }
    
    private void rsyncFiles(String host, String destinationDirectory) throws InterruptedException, IOException{
        String[] cmd = new String[]{"rsync", "-avcz",  EBWT_SRC_DIR, EBWT_USER + "@" + host + ":" + destinationDirectory};
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<cmd.length ; i++){
            sb.append(cmd[i]);
        }
        
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process p = pb.start();
        int val = p.waitFor();
        if (val != 0) {
            log.info("RSYNCING on host: "+host+" with command: "+sb.toString()+" FAILED, val: "+val);
        }else{
            log.info("RSYNCING on host: "+host+" with command: "+sb.toString()+" was SUCCESS, val: "+val);
        }
    }
}
