/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aim4;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author logesh
 */
public class VehicleBasicInfo{

    public int VIN;
    public int position_x;
    public int position_y;
    public float time;
    public List<Integer> neighbours= new ArrayList<Integer>();
    
    public VehicleBasicInfo(){
        VIN=0;
        position_x=0;
        position_y=0;
        time=0;
    }
    public VehicleBasicInfo(int a, int b, int c,float d){
        VIN=a;
        position_x=b;
        position_y=c;
        time=d;
    }

}
