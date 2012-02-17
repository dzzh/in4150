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
 * with other nodes. Ensures that the messages will be delivered in a casual order
 * implemented with Schiper-Eggli-Sandoz algorithm.
 */
public class DA_Schiper_Eggli_Sandoz extends UnicastRemoteObject
        implements DA_Schiper_Eggli_Sandoz_RMI, Runnable{

    private static Log LOGGER = LogFactory.getLog(DA_Schiper_Eggli_Sandoz.class);

    private Map<Integer, List<Integer>> sendBuffer;
    private List<Message> pendingBuffer;
    private Map<String, DA_Schiper_Eggli_Sandoz_RMI> cache;
    private List<Integer> clock;
    private String id;
    private int index;
    private int numProcesses;

    protected DA_Schiper_Eggli_Sandoz(int numProcesses, int index, String id) throws RemoteException {
        super();
        cache = new HashMap<String, DA_Schiper_Eggli_Sandoz_RMI>();
        sendBuffer = new HashMap<Integer, List<Integer>>();
        pendingBuffer = new LinkedList<Message>();
        
        this.index = index;
        this.id = id;
        this.numProcesses = numProcesses;
        this.clock = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++){
            clock.add(0);
        }
    }

    public void run(){
        LOGGER.info("Process started");
    }

    public synchronized void receive(Message message) throws RemoteException {

        LOGGER.info("Receive invoked");
        
        if (message.getDelay() > 0){
            new Thread(new DelayedReceipt(this, message)).start();
            return;
        }

        List<Message> delivered = new LinkedList<Message>();
        
        if (isDeliveryAllowed(message)){
            deliver(message);
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

    public void send(String url, Message message){
        clock.set(index, clock.get(index) + 1);
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
    
    private DA_Schiper_Eggli_Sandoz_RMI getProcess(String url){
        DA_Schiper_Eggli_Sandoz_RMI result = cache.get(url);
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
            cache.put(url, result);
        }
        return result;
    }

    private void deliver(Message message){
        processMessage(message);

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

    private void processMessage(Message message){
        LOGGER.info("Received message " + message.getId() + " from process " + message.getSrcId() +
                    " sent with delay " + message.getDelay() + " at " + System.currentTimeMillis());
    }

    private List<Integer> mergeClocks(List<Integer> clock1, List<Integer> clock2){
        List<Integer> maxClock = new ArrayList<Integer>(numProcesses);
        for (int i = 0; i < numProcesses; i++){
            maxClock.set(i, Math.max(clock1.get(i), clock2.get(i)));
        }
        return maxClock;
    }
    
    private boolean isDeliveryAllowed(Message message){
        boolean result = true;
        List<Integer> clockCopy = new ArrayList<Integer>(clock);
        clockCopy.set(index, clockCopy.get(index) + 1);
        for (int i = 0; i < numProcesses; i++){
            if (clockCopy.get(i) < message.getClock().get(i)){
                result = false;
                break;
            }
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
