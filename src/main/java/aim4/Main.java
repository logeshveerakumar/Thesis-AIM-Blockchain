/*
Copyright (c) 2011 Tsz-Chiu Au, Peter Stone
University of Texas at Austin
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the University of Texas at Austin nor the names of its
contributors may be used to endorse or promote products derived from this
software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package aim4;

import aim4.gui.Viewer;
import aim4.sim.setup.BasicSimSetup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * The default main class to show the GUI.
 */
public class Main {

  /////////////////////////////////
  // THE MAIN FUNCTION
  /////////////////////////////////

  /**
   * The main function of the simulator.
   * It starts the GUI.
   *
   * @param args  the command-line arguments; it should be empty since the GUI
   *              does not take any command-line arguments
     * @throws java.io.IOException
   *
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    
    //Run testrpc to start blockchain client
//    Runtime.getRuntime().exec("killall -9 node"); // kill if any testrpc running   
//    Runtime.getRuntime().exec("testrpc -a 100");  
//    System.out.println("Killing previous instance of testrpc");
//    sleep(4000);  //wait for testrpc init
    VehicleEnvironment.blockchain=false;
    if(VehicleEnvironment.blockchain)
        NodeJS.runNodeJSMethod("nodefile.js","compile");
//    System.out.println("Waiting before testing");
//    NodeJS.runNodeJSMethod("nodefile.js","callContract");
    
      // create the basic setup

    BasicSimSetup simSetup
      = new BasicSimSetup(1, // columns
                          1, // rows
                          4, // lane width
                          25.0, // speed limit
                          1, // lanes per road
                          2, // median size
                          150, // distance between
                          0.28, // traffic level
                          1.0 // stop distance before intersection
                          );

    new Viewer(simSetup);
  }
}

