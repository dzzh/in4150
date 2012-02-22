package nl.tudelft.in4150.da1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Model of a process in a distributed system that can exchange messages
 * with other processes. Ensures that the messages will be delivered in a casual order
 * implemented with Schiper-Eggli-Sandoz algorithm.
 */
public class DA_Schiper_Eggli_Sandoz extends UnicastRemoteObject
        implements DA_Schiper_Eggli_Sandoz_RMI, Runnable{

    private static Log LOGGER = LogFactory.getLog(DA_Schiper_Eggli_Sandoz.class);

    /**
     * ORD_BUFF_S, stores timestamps of latest messages sent to other processes.
     */
    private Map<Integer, List<Integer>> sendBuffer;

    /**
     * B, buffer of messages that cannot be delivered due to delays of previous messages
     */
    private List<Message> pendingBuffer;

    /**
     * Cache to fasten lookup operations in remote registries
     */
    private Map<String, DA_Schiper_Eggli_Sandoz_RMI> processCache;

    /**
     * VT, local vector clock
     */
    private List<Integer> clock;

    /**
     * Index of a current process
     */
    private int index;

    /**
     * Number of processes participating in message exchange
     */
    private int numProcesses;

    /**
     * Default constructor following RMI conventions
     * @param numProcesses number of participating processes
     * @param index index of current process
     * @throws RemoteException if RMI mechanisms fail
     */
    protected DA_Schiper_Eggli_Sandoz(int numProcesses, int index) throws RemoteException {
        super();
        processCache = new HashMap<String, DA_Schiper_Eggli_Sandoz_RMI>();
        sendBuffer = new HashMap<Integer, List<Integer>>();
        pendingBuffer = new LinkedList<Message>();
        
        this.index = index;
        this.numProcesses = numProcesses;
        this.clock = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++){
            clock.add(0);
        }
    }

    /**
     * Multi-threading support
     */
    public void run(){
        LOGGER.info("Process started");
    }

    /** {@inheritDoc} */
    public synchronized void receive(Message message) throws RemoteException {

        /*
         * Artificial mechanism for message delays that allows to test algorithm correctness.
         * If a {@link Message} has delay > 0, new thread starts that tries to resend it after
         * delay number of ms. While this message is delayed, the process is able to receive
         * other messages.
         */
        if (message.getDelay() > 0){
            new Thread(new DelayedReceipt(this, message)).start();
            return;
        }

        if (isDeliveryAllowed(message)){
            deliver(message);

            List<Message> delivered = new LinkedList<Message>();
            for (Message m : pendingBuffer){
                if (isDeliveryAllowed(m)){
                    deliver(m);
                    delivered.add(m);
                }
            }
            pendingBuffer.removeAll(delivered);
            
        } else {
            pendingBuffer.add(message);
        }

    }

    /** {@inheritDoc} */
    public void send(String url, Message message){
        increaseLocalClock();
        DA_Schiper_Eggli_Sandoz_RMI dest = getProcess(url);
        message.setClock(clock);
        message.setSendBuffer(sendBuffer);
        try{
            dest.receive(message);
            sendBuffer.put(dest.getIndex(), clock);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns a process specified by its URL either from a local cache or RMI lookup.
     * @param url process url
     * @return process
     */
    private DA_Schiper_Eggli_Sandoz_RMI getProcess(String url){
        DA_Schiper_Eggli_Sandoz_RMI result = processCache.get(url);
        if (result == null){
            try{
                result = (DA_Schiper_Eggli_Sandoz_RMI)Naming.lookup(url);
            } catch(RemoteException e1){
                e1.printStackTrace();
            } catch (MalformedURLException e2){
                e2.printStackTrace();
            } catch (NotBoundException e3){
                e3.printStackTrace();
            }
            processCache.put(url, result);
        }
        return result;
    }

    /** {@inheritDoc} */
    private void deliver(Message message){
        processMessage(message);
        increaseLocalClock();

        List<Map.Entry<Integer,List<Integer>>> messageBuffer = 
                new ArrayList<Map.Entry<Integer, List<Integer>>>(message.getSendBuffer().entrySet());
        
        for (Map.Entry<Integer, List<Integer>> entry : messageBuffer){
            List<Integer> existingValue = sendBuffer.get(entry.getKey());
            if (existingValue == null){
                sendBuffer.put(entry.getKey(), entry.getValue());
            } else {
                sendBuffer.put(entry.getKey(), mergeClocks(entry.getValue(), sendBuffer.get(entry.getKey())));
            }
        }
    }

    /**
     * A simple action taken upon message delivery
     * @param message delivered message
     */
    private void processMessage(Message message){
        LOGGER.info("Received message " + message.getId() + " from process " + message.getSrcId() +
                    " at " + System.currentTimeMillis());
    }

    /**
     * Updates local clock upon event occurrence.
     */
    private void increaseLocalClock(){
        clock.set(index, clock.get(index) + 1);
    }


    /**
     * Returns maximum of two vector clocks. Values with same indices are compared one-by-one,
     * max is written to the result.
     * @param clock1 first
     * @param clock2 second
     * @return maximum value
     */
    private List<Integer> mergeClocks(List<Integer> clock1, List<Integer> clock2){
        List<Integer> maxClock = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++){
            maxClock.set(i, Math.max(clock1.get(i), clock2.get(i)));
        }
        return maxClock;
    }

    /**
     * Checks whether current message can be delivered based on the accompanying vactor clock state.
     * @param message message to test
     * @return true if the message can be delivered right away, false if the delivery has to be postponed
     * until other messages are delivered.
     */
    private boolean isDeliveryAllowed(Message message){
        
        if (!sendBuffer.containsKey(message.getSrcId()) && !message.getSendBuffer().containsKey(index)){
            return true;
        }
        
        boolean result = true;
        List<Integer> localClockCopy = new ArrayList<Integer>(clock);
        localClockCopy.set(index, localClockCopy.get(index) + 1);
        for (int i = 0; i < numProcesses; i++){
            if (localClockCopy.get(i) > message.getClock().get(i)){
                result = false;
                break;
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    public int getIndex() {
        return index;
    }

}
