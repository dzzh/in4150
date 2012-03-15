package nl.tudelft.in4150.da2;

import nl.tudelft.in4150.da2.message.RequestMessage;
import nl.tudelft.in4150.da2.message.TokenMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implementation of Suzuki-Kasami mutual exclusion algorithm
 */
public class DA_Suzuki_Kasami extends UnicastRemoteObject implements DA_Suzuki_Kasami_RMI, Runnable {

	private static final long serialVersionUID = 2526720373028386278L;
	private static Log LOGGER = LogFactory.getLog(DA_Suzuki_Kasami.class);

    /**
     * Maximum delay simulating a computation unit within {@link #compute()} method and a critical section.
     */
    private static final int MAX_COMPUTATION_DELAY = 1000;

    /**
     * A delay between checks of token acquisition.
     */
    private static final int TOKEN_WAIT_DELAY = 10;
    
    /**
     * Cache to fasten lookup operations in remote registries
     */
    private Map<String, DA_Suzuki_Kasami_RMI> processCache;

	// for every process the sequence number of the last request this process knows about.
    private List<Integer> N;
    
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
    
    private Token token = null;
    private boolean inCriticalSection = false;

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
    @Override
    public void reset(){

        this.N = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++) {
            N.add(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compute() throws RemoteException {
        long t1 = System.currentTimeMillis();
        LOGGER.info("Process " + index + " starts computations.");
        try{
            Thread.sleep(random.nextInt(MAX_COMPUTATION_DELAY));
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        criticalSectionWrapper();

        try{
            Thread.sleep(random.nextInt(MAX_COMPUTATION_DELAY));
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        long t2 = System.currentTimeMillis();
        LOGGER.info("Process " + index + " ends computations which lasted for " + (t2-t1) + " ms.");
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
    @Override
    public void receiveRequest(RequestMessage rm){
        N.set(rm.getSrcId(), rm.getSequence());

        if (!inCriticalSection && token != null && (N.get(rm.getSrcId()) > token.getTN().get(rm.getSrcId())))
        {
            sendToken(rm.getSrcUrl());
        }    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Broadcasts REQUEST message to all the processes
     */
    private void broadcastRequest()
    {
        int i = 0;
        for (String url : urls){
            DA_Suzuki_Kasami_RMI dest = getProcess(url);
            try{
                int sequence = N.get(i) + 1;
                RequestMessage rm = new RequestMessage(urls[index], index, sequence);
                dest.receiveRequest(rm);
            } catch (RemoteException e){
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void receiveToken(TokenMessage tm){
        token = tm.getToken();

        waitForCriticalSectionToStop();

        token.getTN().set(index, N.get(index));

        for (int j = index + 1; j < numProcesses && token != null; j++)
        {
            if(N.get(j) > token.getTN().get(j))
            {
                sendToken(urls[j]);
                break;
            }
        }

        for (int j = 1; j < index - 1 && token != null; j++)
        {
            if(N.get(j) > token.getTN().get(j))
            {
                sendToken(urls[j]);
                break;
            }
        }
    }

    /**
     * Piece of computations requiring mutual exclusion
     */
    private void criticalSection(){
        inCriticalSection = true;
        int delay = random.nextInt(MAX_COMPUTATION_DELAY);
        LOGGER.info("Process " + index + " enters critical section and will compute for " + delay + " ms.");

        try{
            Thread.sleep(delay);
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        LOGGER.info("Process " + index + " leaves critical section.");
        inCriticalSection = false;
    }

    /**
     * Sends token to (remote) process
     * @param url URL of a (remote) process to send token
     */
    private void sendToken(String url){
        assert token != null;
        DA_Suzuki_Kasami_RMI dest = getProcess(url);
        try{
            TokenMessage tm = new TokenMessage(urls[index], index, token);
            dest.receiveToken(tm);
            token = null;
        } catch (RemoteException e){
            throw new RuntimeException(e);
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
     * Invokes {@link #criticalSection()} with all necessary pre- and post- actions according to algorithm
     */
    private void criticalSectionWrapper(){
        
        broadcastRequest();
        waitForToken();
        
        criticalSection();
        
        
    }

    /**
     * Puts process into sleeping state until token is acquired
     */
    private void waitForToken(){
        while (token == null){
            try{
                Thread.sleep(TOKEN_WAIT_DELAY);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Puts process into sleeping state until critical section is done
     */
    private void waitForCriticalSectionToStop(){
        
        //wait for critical section to start working
        try{
            Thread.sleep(TOKEN_WAIT_DELAY);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        //sleep until critical section is done
        while (inCriticalSection){
            try{
                Thread.sleep(TOKEN_WAIT_DELAY);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "DA_Suzuki_Kasami{" +
                "index=" + index +
                ", N=" + N +
                '}';
    }
}

