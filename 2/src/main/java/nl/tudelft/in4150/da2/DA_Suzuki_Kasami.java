package nl.tudelft.in4150.da2;

import nl.tudelft.in4150.da2.message.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class DA_Suzuki_Kasami extends UnicastRemoteObject implements DA_Suzuki_Kasami_RMI, Runnable {

	private static final long serialVersionUID = 2526720373028386278L;
	private static Log LOGGER = LogFactory.getLog(DA_Suzuki_Kasami.class);
    private static final int MAX_DELAY = 1000;
    
    /**
     * Cache to fasten lookup operations in remote registries
     */
    private Map<String, DA_Suzuki_Kasami_RMI> processCache;

	// for every process the sequence number of the last request this process knows about.
    private List<Integer> sequenceNumbers;
    
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
     * Needs to simulate random delays while doing computations
     */
    private Random random = new Random();
    
    /**
     * Default constructor following RMI conventions
     *
     * @param urls URLs of participating processes
     * @param index index of current process
     * @throws RemoteException if RMI mechanisms fail
     */
    protected DA_Suzuki_Kasami(String[] urls, int index) throws RemoteException {
        super();
        processCache = new HashMap<String, DA_Suzuki_Kasami_RMI>();

        this.index = index;
        this.urls = urls;
        this.numProcesses = urls.length;
        
        reset();

    }

    /**
     * {@inheritDoc}
     */
    public void reset(){

        this.sequenceNumbers = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++) {
            sequenceNumbers.add(0);
        }
    }

    /**
     * Multi-threading support
     */
    public void run() {
        LOGGER.info("Process started");
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void receive(Message message) throws RemoteException {

        LOGGER.debug("Received message " + message.getId());

        switch(message.getType()) {
        case REQUEST:
            this.sequenceNumbers.set(message.getSrcId(), message.getSequenceNumbers().get(message.getSrcId()));
            
            if (hasToken && !inCriticalSection &&
                    (sequenceNumbers.get(message.getSrcId()) > token.getSequenceNumbers().get(message.getSrcId())))
            {
            	send(message.getSrcURL(), getToken());
            }
            
        	break;
        	
        case TOKEN:
        	this.setToken(message);
        	
        	/* enter critical section. */
        	
        	token.getSequenceNumbers().set(index, sequenceNumbers.get(index)); // pointer ???
        
        	for (int j = index + 1; j < numProcesses && hasToken; j++)
        	{
        		if(sequenceNumbers.get(j) > token.getSequenceNumbers().get(j))
        		{
        			send(urls[j], getToken());
        			break;
        		}
        	}
        	
        	for (int j = 1; j < index - 1 && hasToken; j++)
        	{
        		if(sequenceNumbers.get(j) > token.getSequenceNumbers().get(j))
        		{
        			send(urls[j], getToken());
        			break;
        		}
        	}
        	
        	break;
        	
        default:
        	// do something clever.
        	break;        
        }
        


    }
    
    public void broadcast(Message message)
    {
    	increaseSequenceNumber();
    	
    	for (String url : urls){
            
            DA_Suzuki_Kasami_RMI dest = getProcess(url);       

            // only the sequence number corresponding with the id of this message is interesting.
            message.setSequenceNumbers(sequenceNumbers);
            
            try {
                dest.receive(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    	}
    }

    /**
     * Returns a process specified by its URL either from a local cache or RMI lookup.
     *
     * @param url process url
     * @return process
     */
    private DA_Suzuki_Kasami_RMI getProcess(String url) {
        DA_Suzuki_Kasami_RMI result = processCache.get(url);
        if (result == null) {
            try {
                result = (DA_Suzuki_Kasami_RMI) Naming.lookup(url);
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

    /**
     * {@inheritDoc}
     */
    public int getIndex() {
        return index;
    }

    /**
     * Performs computations that require critical section access.
     * @throws RemoteException
     */
	@Override
	public void compute() throws RemoteException {
        long t1 = System.currentTimeMillis();
		LOGGER.info("Process " + index + " starts computations.");
        try{
            Thread.sleep(random.nextInt(MAX_DELAY));
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        
        computeInsideCriticalSection();

        try{
            Thread.sleep(random.nextInt(MAX_DELAY));
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        long t2 = System.currentTimeMillis();
        LOGGER.info("Process " + index + " ends computations which lasted for " + (t2-t1) + " ms.");
	}
    
    private void computeInsideCriticalSection(){
        int delay = random.nextInt(MAX_DELAY);
        LOGGER.info("Process " + index + " enters critical section and will compute for " + delay + " ms.");

        try{
            Thread.sleep(delay);
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        
        LOGGER.info("Process " + index + " leaves critical section.");
    }
}
