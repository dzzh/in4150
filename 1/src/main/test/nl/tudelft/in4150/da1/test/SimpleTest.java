package nl.tudelft.in4150.da1.test;

import nl.tudelft.in4150.da1.DA_Schiper_Eggli_Sandoz;
import nl.tudelft.in4150.da1.DA_Schiper_Eggli_Sandoz_RMI;
import nl.tudelft.in4150.da1.Message;
import nl.tudelft.in4150.da1.TestSetup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

public class SimpleTest{
    
    private TestSetup setup;
    
    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }
    
    @Test
    public void testSystem(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        try{
            Message message = new Message(0,process1.getIndex(),process2.getIndex());
            process1.send(setup.getUrls()[process2.getIndex()],message);
        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
}
