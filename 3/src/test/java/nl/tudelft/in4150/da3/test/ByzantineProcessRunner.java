package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;

import java.rmi.RemoteException;


public class ByzantineProcessRunner implements Runnable{

    private DA_Byzantine_RMI process;
    
    public ByzantineProcessRunner(DA_Byzantine_RMI process){
        this.process = process;
    }
    
    @Override
    public void run() {
        try{
            process.start();
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }
}
