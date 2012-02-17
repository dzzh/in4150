package nl.tudelft.in4150.da1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

/**
 * Supports delayed receipt of the message for testing purposes.
 */
public class DelayedReceipt implements Runnable{

    private Log LOGGER = LogFactory.getLog(DelayedReceipt.class);
    
    private DA_Schiper_Eggli_Sandoz process;
    Message message;

    /**
     * Default constructor
     * @param process process to resend a message
     * @param message message to send
     */
    public DelayedReceipt(DA_Schiper_Eggli_Sandoz process, Message message) {
        this.process = process;
        this.message = message;
    }

    /**
     * Sends a message to specified process after a delay retrieved from message, then stops.
     */
    public void run(){
        try{
            LOGGER.info("Message " + message.getId() + " was suddenly delayed for " + message.getDelay() + " ms.");
            Thread.sleep(message.getDelay());
            message.setDelay(0);
            process.receive(message);
            Thread.currentThread().interrupt();
        } catch (InterruptedException e1){
            e1.printStackTrace();
        } catch (RemoteException e2){
            e2.printStackTrace();
        }
    }
}
