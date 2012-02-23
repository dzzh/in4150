package nl.tudelft.in4150.da1;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ProcessManager {

    private static final Log LOGGER = LogFactory.getLog(ProcessManager.class);
    
    private static final String RMI_PREFIX ="rmi://";
    private ArrayList<DA_Schiper_Eggli_Sandoz_RMI> processes;

    /**
     * Launches server instance
     */
    public void startServer(){

        //read network configuration
        Configuration config = null;
        try{
            File configFile = new File("network.cfg");
            if (configFile.exists()){
                config = new PropertiesConfiguration("network.cfg");
            } else {
                config = new PropertiesConfiguration("network.cfg.default");
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        String[] urls = config.getStringArray("node.url");
        processes = new ArrayList<DA_Schiper_Eggli_Sandoz_RMI>();

        //bind local processes and locate remote ones
        int processIndex = 0;
        for (String url : urls){
            try{
                DA_Schiper_Eggli_Sandoz_RMI process;
                if (isProcessLocal(url)){
                    process = new DA_Schiper_Eggli_Sandoz(urls.length, processIndex);
                    LOGGER.debug("Process " + processIndex + " is local.");
                    new Thread((DA_Schiper_Eggli_Sandoz)process).start();
                    Naming.bind(url, process);
                } else {
                    process = (DA_Schiper_Eggli_Sandoz_RMI)Naming.lookup(url);
                    LOGGER.debug("Looking up for process with URL " + url);
                }
                processIndex++;
                processes.add(process);
                
            } catch (RemoteException e1){
                e1.printStackTrace();
            } catch (AlreadyBoundException e2){
                e2.printStackTrace();
            } catch (NotBoundException e3){
                e3.printStackTrace();
            } catch (MalformedURLException e4){
                e4.printStackTrace();
            }
            
        }
    }

    private boolean isProcessLocal(String url){
        return url.startsWith(RMI_PREFIX + "localhost");
    }
    
}
