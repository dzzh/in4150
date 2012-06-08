package nl.tudelft.in4150.da3;

import java.rmi.RemoteException;

/**
 * The class is intended to emulate a delay between filling in the decision tree of a process
 * and taking the decision based on the values in the tree. The delay is needed to let the
 * late messages to be delivered and added to the tree.
 */
public class Waiter implements Runnable {

    //delay in ms
    private final static int DELAY = 100;

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
            Thread.sleep(DELAY);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        try{
            //starts the decision process after the delay
            process.decide();
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }

    public boolean isStarted() {
        return started;
    }
}
