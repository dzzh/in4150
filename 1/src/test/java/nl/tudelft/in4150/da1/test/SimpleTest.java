package nl.tudelft.in4150.da1.test;

import nl.tudelft.in4150.da1.DA_Schiper_Eggli_Sandoz;
import nl.tudelft.in4150.da1.DA_Schiper_Eggli_Sandoz_RMI;
import nl.tudelft.in4150.da1.Message;
import nl.tudelft.in4150.da1.TestSetup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import java.rmi.RemoteException;

public class SimpleTest{
    
    private TestSetup setup;
    private static Log LOGGER = LogFactory.getLog(DA_Schiper_Eggli_Sandoz.class);
    
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
            Message message = new Message(1,process1.getIndex(),process2.getIndex());
            message.setDelay(1000);
            process1.send(setup.getUrls()[process2.getIndex()],message);

            message = new Message(2, process1.getIndex(), process2.getIndex());
            process1.send(setup.getUrls()[process2.getIndex()],message);
                        
            Assert.assertTrue(process2.getReceivedMessages().size() == 2);
            LOGGER.info("process2.getReceivedMessages().size(): " + process2.getReceivedMessages().size());

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    @Ignore
    public void testcase1(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
            Message message = new Message(1,process1.getIndex(),process2.getIndex());
            message.setDelay(0);
            process1.send(setup.getUrls()[process3.getIndex()],message);

            message = new Message(2, process2.getIndex(), process3.getIndex());
            message.setDelay(10);
            process2.send(setup.getUrls()[process3.getIndex()],message);
            
            message = new Message(3, process2.getIndex(), process3.getIndex());
            message.setDelay(20);
            process1.send(setup.getUrls()[process3.getIndex()],message);
            
            Assert.assertTrue(process2.getReceivedMessages().size() == 1);
            Assert.assertTrue(process3.getReceivedMessages().size() == 2);

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    @Ignore
    public void testcase2(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
            Message message = new Message(1,process1.getIndex(),process3.getIndex());
            message.setDelay(30);
            process1.send(setup.getUrls()[process3.getIndex()],message);

            message = new Message(2, process1.getIndex(), process2.getIndex());
            message.setDelay(10);
            process1.send(setup.getUrls()[process2.getIndex()],message);
            
            message = new Message(3, process2.getIndex(), process3.getIndex());
            message.setDelay(20);
            process2.send(setup.getUrls()[process3.getIndex()],message);
            
            Assert.assertTrue(process2.getReceivedMessages().size() == 1);
            Assert.assertTrue(process3.getReceivedMessages().size() == 2);

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    @Ignore
    public void testcase3(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
            Message message = new Message(1,process1.getIndex(),process2.getIndex());
            message.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message);

            message = new Message(2, process2.getIndex(), process3.getIndex());
            message.setDelay(10);
            process2.send(setup.getUrls()[process3.getIndex()],message);
            
            message = new Message(3, process1.getIndex(), process3.getIndex());
            message.setDelay(20);
            process1.send(setup.getUrls()[process3.getIndex()],message);
            
            Assert.assertTrue(process2.getReceivedMessages().size() == 1);
            Assert.assertTrue(process3.getReceivedMessages().size() == 2);

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    @Ignore
    public void testcase4(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);

        try{
        	// This message, m1, will arrive after, m2, thus late.
            Message message = new Message(1,process1.getIndex(),process2.getIndex());
            message.setDelay(20);
            process1.send(setup.getUrls()[process2.getIndex()],message);

            message = new Message(2, process1.getIndex(), process2.getIndex());
            message.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message);
            
            Assert.assertTrue(process2.getReceivedMessages().size() == 2);
            
        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    @Ignore
    public void testcase5(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
        	// This message, m1, will arrive after, m2, thus late.
            Message message = new Message(1,process1.getIndex(),process2.getIndex());
            message.setDelay(20);
            process1.send(setup.getUrls()[process2.getIndex()],message);

            message = new Message(2, process1.getIndex(), process2.getIndex());
            message.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message);
            
            message = new Message(3, process2.getIndex(), process3.getIndex());
            message.setDelay(10);
            process2.send(setup.getUrls()[process3.getIndex()],message);
            
            Assert.assertTrue(process2.getReceivedMessages().size() == 2);
            Assert.assertTrue(process3.getReceivedMessages().size() == 1);

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
}
