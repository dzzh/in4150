package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;

public class TestThread implements Runnable{

    private DA_Byzantine_RMI process;
    
    public TestThread(DA_Byzantine_RMI process){
        this.process = process;
    }
    
    @Override
    public void run() {

    }
}