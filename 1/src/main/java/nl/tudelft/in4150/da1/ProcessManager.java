package nl.tudelft.in4150.da1;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ProcessManager {

    private static final Log LOGGER = LogFactory.getLog(ProcessManager.class);

    private static final String RMI_PREFIX = "rmi://";
    private ArrayList<DA_Schiper_Eggli_Sandoz_RMI> processes;
    private InetAddress inetAddress;
    private static final int INSTANTIATION_DELAY = 5000;

    /**
     * Launches server instance
     */
    public void startServer() {

        //read network configuration
        Configuration config = null;
        try {
            config = new PropertiesConfiguration("network.cfg");
        } catch (ConfigurationException e) {
            try {
                config = new PropertiesConfiguration("network.cfg.default");
            } catch (ConfigurationException e2) {
                e2.printStackTrace();
            }
        }

        //instantiating InetAddress to resolve local IP
        try{
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e){
            LOGGER.error("Cannot instantiate IP resolver");
            throw new RuntimeException(e);
        }

        assert config != null;
        String[] urls = config.getStringArray("node.url");
        processes = new ArrayList<DA_Schiper_Eggli_Sandoz_RMI>();

        //bind local processes and locate remote ones

        try {
            DA_Schiper_Eggli_Sandoz_RMI process = null;
            int processIndex = 0;
            for (String url : urls) {
                if (isProcessLocal(url)) {
                    process = new DA_Schiper_Eggli_Sandoz(urls.length, processIndex);
                    LOGGER.debug("Process " + processIndex + " is local.");
                    new Thread((DA_Schiper_Eggli_Sandoz) process).start();
                    Naming.bind(url, process);
                    processes.add(process);
                } 
                processIndex++;


            }

            //sleep is needed to instantiate local processes at all machines
           LOGGER.debug("Waiting for remote processes to instantiate...");
            try{
                Thread.sleep(INSTANTIATION_DELAY);
            } catch(InterruptedException e){
                throw new RuntimeException(e);
            }
            LOGGER.debug("And looking them up.");
            
            for (String url : urls){
                if (!isProcessLocal(url)) {
                    process = (DA_Schiper_Eggli_Sandoz_RMI) Naming.lookup(url);
                    LOGGER.debug("Found remote process with URL " + url);
                    processes.add(process);
                }
            }
            
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } catch (AlreadyBoundException e2) {
            e2.printStackTrace();
        } catch (NotBoundException e3) {
            e3.printStackTrace();
        } catch (MalformedURLException e4) {
            e4.printStackTrace();
        }
    }

    private boolean isProcessLocal(String url){
        return url.startsWith(RMI_PREFIX + inetAddress.getHostAddress()) ||
                url.startsWith(RMI_PREFIX + "localhost") ||
                url.startsWith(RMI_PREFIX + "127.0.0.1");
    }

}
