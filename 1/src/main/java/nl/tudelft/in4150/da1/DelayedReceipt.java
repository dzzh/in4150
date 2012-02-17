package nl.tudelft.in4150.da1;

import java.rmi.RemoteException;

public class DelayedReceipt implements Runnable{

    private DA_Schiper_Eggli_Sandoz process;
    Message message;

    public DelayedReceipt(DA_Schiper_Eggli_Sandoz process, Message message) {
        this.process = process;
        this.message = message;
    }
    
    public void run(){
        try{
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
