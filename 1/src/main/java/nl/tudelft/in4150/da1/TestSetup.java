package nl.tudelft.in4150.da1;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Initiates values needed for testing
 */
public class TestSetup {

    private List<DA_Schiper_Eggli_Sandoz_RMI> processes;
    private String[] urls;

    public TestSetup() { }

    public void init(){

        //read network configuration
        Configuration config = null;
        try{
            config = new PropertiesConfiguration("network.cfg");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        urls = config.getStringArray("node.url");
        processes = new ArrayList<DA_Schiper_Eggli_Sandoz_RMI>();

        //locate processes
        for (String url : urls){
            try{
                DA_Schiper_Eggli_Sandoz_RMI process = (DA_Schiper_Eggli_Sandoz_RMI) Naming.lookup(url);
                process.reset();
                processes.add(process);

            } catch (RemoteException e1){
                e1.printStackTrace();
            } catch (NotBoundException e2){
                e2.printStackTrace();
            } catch (MalformedURLException e3){
                e3.printStackTrace();
            }

        }
    }

    public List<DA_Schiper_Eggli_Sandoz_RMI> getProcesses() {
        return processes;
    }

    public String[] getUrls() {
        return urls;
    }
}
