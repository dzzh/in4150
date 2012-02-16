package nl.tudelft.in4150.da1;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Main class, starts the application and initiates RMI registry.
 */
public class DA_Schiper_Eggli_Sandoz_Main {
    
    private static Log LOGGER = LogFactory.getLog(DA_Schiper_Eggli_Sandoz_Main.class);
    
    public static void main(String[] args){
        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        Configuration config = null;
        try{
            config = new PropertiesConfiguration("classpath:network.cfg");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        String[] urls = config.getStringArray("node.url");
        LOGGER.info(urls[0]);
    }
    
}
                                             