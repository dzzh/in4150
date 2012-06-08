package nl.tudelft.in4150.da3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

/**
 * The class is intended to emulate a delay between filling in the decision tree of a process
 * and taking the decision based on the values in the tree. The delay is needed to let the
 * late messages to be delivered and added to the tree.
 */
public class Waiter implements Runnable {

    private static Log LOGGER = LogFactory.getLog(Waiter.class);

    //delay in ms
    private final static int DELAY = 2000;

    private DA_Byzantine_RMI process;
    private boolean started = false;

    public Waiter(DA_Byzantine_RMI process) {
        this.process = process;
    }

    @Override
    public void run() {

        //is needed to make sure that the waiter runs only once per each process
        started = true;

        try{
            LOGGER.debug("[" + process.getIndex() + "] waiter started");
            //wait for late messages
            Thread.sleep(DELAY);
            //starts the decision process after the delay
            process.decide();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }

    public boolean isStarted() {
        return started;
    }
}
