package nl.tudelft.in4150.da2.test;

import nl.tudelft.in4150.da2.DA_Suzuki_Kasami_RMI;
import nl.tudelft.in4150.da2.Token;
import nl.tudelft.in4150.da2.TestSetup;
import nl.tudelft.in4150.da2.message.TokenMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

public class SimpleTest{
    
    private TestSetup setup;

    private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }
    
    @Test
    public void testCase1(){
        DA_Suzuki_Kasami_RMI process1 = setup.getProcesses().get(0);
        TestThread thread1 = new TestThread(process1);
        DA_Suzuki_Kasami_RMI process2 = setup.getProcesses().get(1);
        TestThread thread2 = new TestThread(process2);
        DA_Suzuki_Kasami_RMI process3 = setup.getProcesses().get(2);
        TestThread thread3 = new TestThread(process3);

        try{
            Token token = Token.instantiate(3);
            TokenMessage tm = new TokenMessage("",0,token);
            new Thread(thread1).start();
            process1.receiveToken(tm);
            new Thread(thread2).start();
            new Thread(thread3).start();
            
        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }    
}
