/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aim4;

import static aim4.VehicleEnvironment.hmap;
import static aim4.VehicleEnvironment.totalVehicleCrossed;
import static aim4.VehicleEnvironment.totalVehicleVerified;
import static aim4.VehicleEnvironment.totalVehicleVerifiedBySensorOnly;
import static aim4.sim.AutoDriverOnlySimulator.vinToVehicles;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author logesh
 */
public class VehicleEnvironment {
    

    
    public static HashMap<Integer,VehicleBasicInfo> hmap= new HashMap<Integer, VehicleBasicInfo>();
    public static boolean blockchain = true;
    public static int totalVehicleCrossed=0;
    public static int totalVehicleVerified=0;
    public static int totalVehicleVerifiedBySensorOnly=0;
    public static float averageResponseTime=0;
    public static float sumResponseTime=0;
    public static int totalVeh=0;
    
    
//    public static boolean findNeighbours(int vin){
//        
//        if(hmap.get(vin).neighbours.size()>0)//return if there exist a neighbour before
//                return true;
//        //System.out.println("hai:"+hmap.get(vin).neighbours.size());
//        
//        Iterator it = hmap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            int temp_vin=(Integer)pair.getKey();
//            VehicleBasicInfo temp = (VehicleBasicInfo)pair.getValue();
//            
//            
//            
//            if(vin!=temp_vin){
//                if((temp.time - hmap.get(vin).time)<=2 && (temp.time - hmap.get(vin).time)>= -2){
//                    if ((temp.position_x - hmap.get(vin).position_x)*(temp.position_x - hmap.get(vin).position_x) + (temp.position_y - hmap.get(vin).position_y)*(temp.position_x - hmap.get(vin).position_y) <= 5*5){
//                        if(hmap.get(vin).neighbours.contains(temp_vin)==false)
//                            hmap.get(vin).neighbours.add(temp_vin);
//                        if(hmap.get(temp_vin).neighbours.contains(vin)==false)
//                            hmap.get(temp_vin).neighbours.add(vin);
//                        
//                    }
//                }
//                
//            }
//            
//            //it.remove(); // avoids a ConcurrentModificationException
//        }
//        
//        //System.out.println(hmap.get(vin).neighbours.size());
//        if(hmap.get(vin).neighbours.size()>0)//return if there exist a neighbour before
//            return true;
//        else
//            return false;
//    }
   
    public static boolean findNeighbour(int vin){
        
//        if(totalVehicleCrossed%100==0 && totalVehicleCrossed!=0){
//            System.out.println("Total Vehicle Crossed So far is "+totalVehicleCrossed);
//            System.out.println("Total Vehicle Verified is "+totalVehicleVerified);
//            System.out.println("In that, total vehicle verified only by sensor on site is "+totalVehicleVerifiedBySensorOnly);
//        }
//        
        if(hmap.get(vin).neighbours.size()>0)//return if there exist a neighbour before
        {   totalVehicleCrossed++;
            totalVehicleVerified++;
            return true;
        }
        
        for(VehicleSimView vehicle : vinToVehicles.values()) {
            AutoVehicleSimView temp = (AutoVehicleSimView)vehicle;
            int vehicle_position_x;
            int vehicle_position_y;
            vehicle_position_x = (int)temp.getPosition().getX();
            vehicle_position_y = (int)temp.getPosition().getY();
            
            
            
            
            if(vin!=temp.getVIN()){
                //if((temp.time - hmap.get(vin).time)<=2 && (temp.time - hmap.get(vin).time)>= -2){
                    if ((vehicle_position_x - hmap.get(vin).position_x)*(vehicle_position_x - hmap.get(vin).position_x) + (vehicle_position_y - hmap.get(vin).position_y)*(vehicle_position_y - hmap.get(vin).position_y) <= 10*10){
                        if(hmap.get(vin).neighbours.contains(temp.getVIN())==false)
                            hmap.get(vin).neighbours.add(temp.getVIN());
                        try{
                            if(hmap.get(temp.getVIN()).neighbours.contains(vin)==false)
                                hmap.get(temp.getVIN()).neighbours.add(vin);
                        }
                        catch(Exception e){
                            System.out.print(""); 
                        }
                        
                    }
                //}
                //System.out.println("test: "+vehicle_position_x+" "+vehicle_position_y+" "+hmap.get(vin).position_x+" "+hmap.get(vin).position_y);
                
            }
        }
        
        boolean sensorFlag=false;
        //The sensor at the jnction center is 158,158
        if ((158 - hmap.get(vin).position_x)*(158 - hmap.get(vin).position_x) + (158 - hmap.get(vin).position_y)*(158 - hmap.get(vin).position_y) <= 10*10)
            sensorFlag=true;
        
        if(hmap.get(vin).neighbours.size()>0)//return if there exist a neighbour before
        {   
            System.out.print("Neighbours:"+hmap.get(vin).neighbours);
            if(sensorFlag)
                System.out.println(" & sensor on site");
            else
                System.out.println();
            totalVehicleCrossed++;
            totalVehicleVerified++;
            return true;
            }
        else {
            System.out.println("Witnessed only by sensor on site");
            totalVehicleVerifiedBySensorOnly++;
            totalVehicleCrossed++;
            return true;
        } 
            
    }
    
 

}

