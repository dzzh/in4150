package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;

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

    private List<DA_Byzantine_RMI> processes;
    private String[] urls;

    public TestSetup() { }

    public void init(){

        //read network configuration
        Configuration config = null;
        try{
            config = new PropertiesConfiguration("network.cfg");
        } catch (ConfigurationException e) {
            try{
                config = new PropertiesConfiguration("network.cfg.default");
            } catch (ConfigurationException e2) {
                throw new RuntimeException(e2);
            }
        }

        urls = config.getStringArray("node.url");
        processes = new ArrayList<DA_Byzantine_RMI>();

        //locate processes
        for (String url : urls){
            try{
                DA_Byzantine_RMI process = (DA_Byzantine_RMI) Naming.lookup(url);
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

    public List<DA_Byzantine_RMI> getProcesses() {
        return processes;
    }

    public String[] getUrls() {
        return urls;
    }
}
