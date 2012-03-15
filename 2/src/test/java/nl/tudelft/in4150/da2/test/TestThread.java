package nl.tudelft.in4150.da2.test;

import nl.tudelft.in4150.da2.DA_Suzuki_Kasami_RMI;

import java.rmi.RemoteException;

public class TestThread implements Runnable{

    private DA_Suzuki_Kasami_RMI process;
    
    public TestThread(DA_Suzuki_Kasami_RMI process){
        this.process = process;
    }
    
    @Override
    public void run() {
        try{
            process.compute();
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }
}
