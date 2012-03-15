package nl.tudelft.in4150.da2;

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

	// all received messeges.
    private LinkedList<Message> receivedMessages;

    /**
     * Cache to fasten lookup operations in remote registries
     */
    private Map<String, DA_Suzuki_Kasami_RMI> processCache;

	// for every process the sequence number of the last request this process knows about.
    private List<Integer> sequenceNumbers;
    
    private Message token;

    /**
     * Index of a current process
     */
    private int index;

    /**
     * Number of processes participating in message exchange
     */
    private int numProcesses;

    private String[] urls;
    
    private boolean hasToken; 
    
    private boolean inCriticalSection;
    
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

        receivedMessages = new LinkedList<Message>();

        this.sequenceNumbers = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++) {
            sequenceNumbers.add(0);
        }

        this.hasToken = index == 0;
        
        this.inCriticalSection = false;
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

        /*
         * Artificial mechanism for message delays that allows to test algorithm correctness.
         * If a {@link Message} has delay > 0, new thread starts that tries to resend it after
         * delay number of ms. While this message is delayed, the process is able to receive
         * other messages.
         */
        if (message.getDelay() > 0) {
            new Thread(new DelayedReceipt(this, message)).start();
            return;
        }

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
     * {@inheritDoc}
     */
    public void send(String url, Message token) {
        DA_Suzuki_Kasami_RMI dest = getProcess(url);       
        
        try {
            dest.receive(token);
        } catch (RemoteException e) {
            e.printStackTrace();
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
                e1.printStackTrace();
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
            } catch (NotBoundException e3) {
                e3.printStackTrace();
            }
            processCache.put(url, result);
        }
        return result;
    }

    private void increaseSequenceNumber() {
        sequenceNumbers.set(index, sequenceNumbers.get(index) + 1);
    }

    /**
     * {@inheritDoc}
     */
    public int getIndex() {
        return index;
    }
    
    public void setToken(Message token)
    {
    	this.token = token;
    	this.hasToken = true;
    }
    
    public Message getToken()
    {
    	Message token = this.token;
    	this.token = null;
    	this.hasToken = false;
    	return token;
    }

    @Override
    public LinkedList<Message> getMessages() throws RemoteException {
        return receivedMessages;
    }

	@Override
	public void accessCS(int processIndex) throws RemoteException {
		// Test if the critical sections is already taken.
		
	}
}
