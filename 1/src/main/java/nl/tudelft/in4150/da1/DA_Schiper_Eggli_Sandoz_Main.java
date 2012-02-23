package nl.tudelft.in4150.da1;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Main class to initiate RMI registry and start servers
 */
public class DA_Schiper_Eggli_Sandoz_Main {
    
    public static void main(String[] args){

        //RMI registry initialization
        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        //Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        new ProcessManager().startServer();
    }

}
                                             