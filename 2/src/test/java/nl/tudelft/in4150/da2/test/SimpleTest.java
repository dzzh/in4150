package nl.tudelft.in4150.da2.test;

import nl.tudelft.in4150.da2.DA_Suzuki_Kasami_RMI;
import nl.tudelft.in4150.da2.Message;
import nl.tudelft.in4150.da2.TestSetup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.rmi.RemoteException;
import java.util.List;

public class SimpleTest{
    
    private TestSetup setup;

    private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }
    
    @Test
    //@Ignore
    /**
     * P1 sends m1 to P2
     * P1 sends m2 to P2 but m2 arrives before m1
     */
    public void testCase1(){
        DA_Suzuki_Kasami_RMI process1 = setup.getProcesses().get(0);
        DA_Suzuki_Kasami_RMI process2 = setup.getProcesses().get(1);
        try{
            Message message1 = new Message(1, Message.Type.REQUEST, setup.getUrls()[process1.getIndex()], process1.getIndex(), process2.getIndex());
            message1.setDelay(10);
            //process1.send(setup.getUrls()[process2.getIndex()],message1);

            Message message2 = new Message(2, Message.Type.REQUEST, setup.getUrls()[process1.getIndex()], process1.getIndex(), process2.getIndex());
            //process1.send(setup.getUrls()[process2.getIndex()],message2);

            // Sleep atleast the sum of all delays to be sure all messages have arrived.
            Thread.sleep(20);

            List<Message> messages = process2.getMessages();
            Assert.assertTrue(2 == messages.size());
            Assert.assertTrue(message1.getId() == messages.get(0).getId());
            Assert.assertTrue(message2.getId() == messages.get(1).getId());

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e){
            e.printStackTrace();
            Assert.fail();
        }

    }    
}
