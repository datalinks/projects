/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalcollect.infra2.ebwtool.util;

import static com.globalcollect.infra2.ebwtool.config.Variables.EBWT_USER;
import com.globalcollect.infra2.ebwtool.model.Environments;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author cvugrine
 */
public class EbwCopyToolHelper {

    static Logger log = Logger.getLogger(EbwCopyToolHelper.class.getName());

    static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    static String getLastElement(String theString, String theSeparator) {
        String[] values = theString.split(theSeparator);
        return values[values.length - 1];

    }

    static Environments getEnvironment(String env) {
        Environments result = null;
        if (env.equalsIgnoreCase(Environments.DEV.toString())) {
            result = Environments.DEV;
        }
        if (env.equalsIgnoreCase(Environments.STAG.toString())) {
            result = Environments.STAG;
        }
        if (env.equalsIgnoreCase(Environments.PRD.toString())) {
            result = Environments.PRD;
        }
        if (env.equalsIgnoreCase(Environments.TEST.toString())) {
            result = Environments.TEST;
        }

        return result;
    }

    private static Session getSshSession(String privateKeyFile, String user, String hostname) throws JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyFile);
        Session session = jsch.getSession(user, hostname);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return session;
    }

    public static void executeRemoteCommand(String key, String user, String host, String command) throws Exception {
        
        String msg = null;
        DataInputStream dataIn = null;
        DataOutputStream dataOut = null;
        ChannelExec channel = null;
        Session session = null;

        try{
            
            session = getSshSession(key, user, host);
            channel=(ChannelExec) session.openChannel("exec");
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.setCommand(command);
            channel.setErrStream(System.err);
            channel.connect();

            if(log.isDebugEnabled()){
                while((msg=in.readLine())!=null){
                 log.debug(msg);
                }                
            }
            String error = convertStreamToString(channel.getErrStream());
            if(!error.isEmpty()){
                msg = "Something went wrong executing : "+command+" on host: "+host+" error: "+error;
                log.error(msg);
                throw new Exception(msg);
            }
        }catch(JSchException ex){
            msg = "Exception during executeRemoteCommand, msg was: "+ex.getMessage();
            log.error(msg);
            throw new Exception(msg);
        }catch(IOException ex){
            msg = "IOException during executeRemoteCommand, msg was: "+ex.getMessage();
            log.error(msg);
            throw new Exception(msg);
        }finally{
            log.debug("disconnecting session and channel");
            channel.disconnect();
            session.disconnect();
        }
    }

    static String convertStreamToString(InputStream is) throws IOException{
        int k;
        StringBuffer sb=new StringBuffer();
        while((k=is.read())!=-1){
            sb.append((char)k);
        }
        return sb.toString();
    }
    
}
