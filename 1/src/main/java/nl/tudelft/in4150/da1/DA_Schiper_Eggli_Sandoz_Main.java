package nl.tudelft.in4150.da1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class DA_Schiper_Eggli_Sandoz_Main {
    
    public static void main(String[] args){
        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }
    
}
                                             