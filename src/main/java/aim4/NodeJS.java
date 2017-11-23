/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aim4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author logesh
 */
public class NodeJS{

    public static String runNodeJSMethod(String nodefile, String variables) throws IOException{
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"node","src/main/java/aim4/nodejs_methods/"+ nodefile,variables};
        boolean flag=true;
        String tempS="";
        
        while(true){
            flag=true;
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(proc.getErrorStream()));
            
            
            while ((tempS = stdError.readLine()) != null) {
            if(tempS.contains("Invalid JSON RPC response"))
                flag=false;
            }
            if(flag==false)
                continue;
            


            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            String s = null;
            String output = "";
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                output=output+s;
            }

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(""+s);
            }

            return output;
            
        }
        
        
        
  }

}
