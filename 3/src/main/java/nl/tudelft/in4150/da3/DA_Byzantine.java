package nl.tudelft.in4150.da3;

import nl.tudelft.in4150.da3.message.AckMessage;
import nl.tudelft.in4150.da3.message.Message;
import nl.tudelft.in4150.da3.message.OrderMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implementation of randomized Byzantine agreement without authentication algorithm (Lamport-Pease-Shostak).
 * Synchronous communication is modelled by sending each message from node to other nodes each round.
 */
public class DA_Byzantine extends UnicastRemoteObject implements DA_Byzantine_RMI, Runnable {

    private static final long serialVersionUID = 2526720373028386278L;
    private static Log LOGGER = LogFactory.getLog(DA_Byzantine.class);

    /**
     * Cache to fasten lookup operations in remote registries
     */
    private Map<String, DA_Byzantine_RMI> processCache;

    /**
     * Index of a current process
     */
    private int index;

    /**
     * Number of processes participating in message exchange
     */
    private int numProcesses;

    /**
     * URLs of processes in a system
     */
    private String[] urls;

    /**
     * Counter of a messages sent by the process
     */
    private int nextMessageId = 1;
    
    
    /**
     * Default constructor following RMI conventions
     *
     * @param urls  URLs of participating processes
     * @param index index of current process
     * @throws RemoteException if RMI mechanisms fail
     */
    protected DA_Byzantine(String[] urls, int index) throws RemoteException {
        super();
        processCache = new HashMap<String, DA_Byzantine_RMI>();

        this.index = index;
        this.urls = urls;
        this.numProcesses = urls.length;

        reset();
    }
    
    @Override
    public void receiveOrder(OrderMessage message) throws RemoteException{
    	
    }
    
    @Override 
    public void receiveAck(AckMessage message) throws RemoteException{
    	
    }
    
    
    /**
     * Constructs a template of a new message
     * @param receiver
     * @return
     */
    private Message getMessageTemplate(int receiver){
    	nextMessageId++;
    	return new Message(nextMessageId - 1, index, receiver);
    }
    
    @Override
    public void reset(){

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return index;
    }
       
    @Override
	public boolean isFaulty() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFaulty(boolean isFaulty) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	/**
     * Returns a process specified by its URL either from a local cache or RMI lookup.
     *
     * @param url process url
     * @return process
     */
    private DA_Byzantine_RMI getProcess(String url) {
        DA_Byzantine_RMI result = processCache.get(url);
        if (result == null) {
            try {
                result = (DA_Byzantine_RMI) Naming.lookup(url);
            } catch (RemoteException e1) {
                throw new RuntimeException(e1);
            } catch (MalformedURLException e2) {
                throw new RuntimeException(e2);
            } catch (NotBoundException e3) {
                throw new RuntimeException(e3);
            }
            processCache.put(url, result);
        }
        return result;
    }

    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

