package nl.tudelft.in4150.da1.test;

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
    public void testSystem(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        try{
            Message message1 = new Message(1,process1.getIndex(),process2.getIndex());
            message1.setDelay(10);
            process1.send(setup.getUrls()[process2.getIndex()],message1);

            Message message2 = new Message(2, process1.getIndex(), process2.getIndex());
            process1.send(setup.getUrls()[process2.getIndex()],message2);

            // Sleep at least the sum of all delays to be sure all messages have arrived.
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
    
    @Test
    //@Ignore
    /**
     * P1 sends m1 to P2 
     * P2 sends m2 to P3 
     * P1 sends m3 to P3
     */
    public void testcase1(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
            Message message1 = new Message(1,process1.getIndex(),process2.getIndex());
            message1.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message1 );

            Message message2  = new Message(2, process2.getIndex(), process3.getIndex());
            message2.setDelay(10);
            process2.send(setup.getUrls()[process3.getIndex()], message2 );
            
            Message message3  = new Message(3, process2.getIndex(), process3.getIndex());
            message3.setDelay(20);
            process1.send(setup.getUrls()[process3.getIndex()],message3 );
            
            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(50);

            List<Message> messagesProcess2 = process2.getMessages();            
            Assert.assertTrue(1 == messagesProcess2.size());
            Assert.assertTrue(message1.getId() == messagesProcess2.get(0).getId());
            
            List<Message> messagesProcess3 = process3.getMessages();
            Assert.assertTrue(2 == messagesProcess3.size());
            Assert.assertTrue(message2.getId() == messagesProcess3.get(0).getId());
            Assert.assertTrue(message3.getId() == messagesProcess3.get(1).getId());

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
		}
    }
    
    @Test
    //@Ignore
    /**
     * Figure 3.5 of lecture notes.
     */
    public void testcase2(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
            Message message1 = new Message(1, process1.getIndex(), process3.getIndex());
            message1.setDelay(30);
            process1.send(setup.getUrls()[process3.getIndex()], message1);

            Message message2 = new Message(2, process1.getIndex(), process2.getIndex());
            message2.setDelay(10);
            process1.send(setup.getUrls()[process2.getIndex()], message2);
            
            Thread.sleep(20);
            
            Message message3 = new Message(3, process2.getIndex(), process3.getIndex());
            process2.send(setup.getUrls()[process3.getIndex()], message3);
            
            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(150);

            List<Message> messagesProcess2 = process2.getMessages();
            Assert.assertTrue(1 == messagesProcess2.size());
            Assert.assertTrue(message2.getId() == messagesProcess2.get(0).getId());
            
            List<Message> messagesProcess3 = process3.getMessages();
            Assert.assertTrue(2 == messagesProcess3.size());
            
            for (int i=0; i < messagesProcess3.size(); i++)
            {
            	LOGGER.info("messagesProcess3.get(" + i + ").getId(): " + messagesProcess3.get(i).getId());
            }
            
            Assert.assertTrue(message1.getId() == messagesProcess3.get(0).getId());
            Assert.assertTrue(message3.getId() == messagesProcess3.get(1).getId());

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
		}
    }
    
    @Test
    //@Ignore
    /**
     * P1 sends m1 to P2
	 * P2 sends m2 to P3
	 * P1 sends m3 to P3 but m3 arrives before m2
     */
    public void testcase3(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
            Message message1 = new Message(1,process1.getIndex(),process2.getIndex());
            message1.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message1);

            Message message2 = new Message(2, process2.getIndex(), process3.getIndex());
            message2.setDelay(10);
            process2.send(setup.getUrls()[process3.getIndex()],message2);
            
            Message message3 = new Message(3, process1.getIndex(), process3.getIndex());
            message3.setDelay(20);
            process1.send(setup.getUrls()[process3.getIndex()],message3);
            
            // Sleep atleast the sum of all delays to be sure all messages have arrived.
            Thread.sleep(150);

            List<Message> messagesProcess2 = process2.getMessages();

            Assert.assertTrue(1 == messagesProcess2.size());
            Assert.assertTrue(message1.getId() == messagesProcess2.get(0).getId());
            
            List<Message> messagesProcess3 = process3.getMessages();

            Assert.assertTrue(2 == messagesProcess3.size());
            Assert.assertTrue(message2.getId() == messagesProcess3.get(0).getId());
            Assert.assertTrue(message3.getId() == messagesProcess3.get(1).getId());

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
		}
    }
    
    @Test
    //@Ignore
    /**
     * P1 sends m1 to P2
	 * P1 sends m2 to P2 but m2 arrives before m1
     */
    public void testcase4(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);

        try{
        	// This message, m1, will arrive after, m2, thus late.
            Message message1 = new Message(1,process1.getIndex(),process2.getIndex());
            message1.setDelay(20);
            process1.send(setup.getUrls()[process2.getIndex()],message1);

            Message message2 = new Message(2, process1.getIndex(), process2.getIndex());
            message2.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message2);
            
            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(150);

            List<Message> messagesProcess2 = process2.getMessages();

            Assert.assertTrue(2 == messagesProcess2.size());
            Assert.assertTrue(message1.getId() == messagesProcess2.get(0).getId());
            Assert.assertTrue(message2.getId() == messagesProcess2.get(1).getId());
            
        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
		}
    }

    @Test
    //@Ignore
    /**
     * P1 sends m1 to P2
	 * P1 sends m2 to P2 but m2 arrives before m1 and m2 depends on m1
	 * P2 sends m3 to P3
     */
    public void testcase5(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);

        try{
        	// This message, m1, will arrive after, m2, thus late.
            Message message1 = new Message(1,process1.getIndex(),process2.getIndex());
            message1.setDelay(20);
            process1.send(setup.getUrls()[process2.getIndex()],message1);

            Message message2 = new Message(2, process1.getIndex(), process2.getIndex());
            message2.setDelay(0);
            process1.send(setup.getUrls()[process2.getIndex()],message2);
            
            Message message3 = new Message(3, process2.getIndex(), process3.getIndex());
            message3.setDelay(10);
            process2.send(setup.getUrls()[process3.getIndex()],message3);
            
            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(150);

            List<Message> messagesProcess2 = process2.getMessages();

            Assert.assertTrue(2 == messagesProcess2.size());
            Assert.assertTrue(message1.getId() == messagesProcess2.get(0).getId());
            Assert.assertTrue(message2.getId() == messagesProcess2.get(1).getId());
            
            List<Message> messagesProcess3 = process3.getMessages();

            Assert.assertTrue(1 == messagesProcess3.size());
            Assert.assertTrue(message3.getId() == messagesProcess3.get(0).getId());

        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
		}
    }
    
    @Test
    //@Ignore
    /**
	 * See Figure 5 in "A New Algorithm to Implement Causal Ordering."
     */
    public void testcaseSchiperEggliSandoz(){
        DA_Schiper_Eggli_Sandoz_RMI process1 = setup.getProcesses().get(0);
        DA_Schiper_Eggli_Sandoz_RMI process2 = setup.getProcesses().get(1);
        DA_Schiper_Eggli_Sandoz_RMI process3 = setup.getProcesses().get(2);
        DA_Schiper_Eggli_Sandoz_RMI process4 = setup.getProcesses().get(3);

        try{
        	// Messages of S1 (= process1)
            Message message1 = new Message(1,process1.getIndex(),process3.getIndex());
            message1.setDelay(60);
            process1.send(setup.getUrls()[process3.getIndex()], message1);

            Message message2 = new Message(2, process1.getIndex(), process2.getIndex());
            message2.setDelay(10);
            process1.send(setup.getUrls()[process2.getIndex()], message2);
            
            Message message3 = new Message(3, process1.getIndex(), process4.getIndex());
            message3.setDelay(40);
            process1.send(setup.getUrls()[process4.getIndex()], message3);
            
            Thread.sleep(20);
            
            // Messages of S2 (= process2)
            Message message4 = new Message(4,process2.getIndex(),process3.getIndex());
            process2.send(setup.getUrls()[process3.getIndex()], message4);

            Message message5 = new Message(5, process2.getIndex(), process4.getIndex());
            process2.send(setup.getUrls()[process4.getIndex()], message5);
            
            Thread.sleep(20);
            
            // Messages of S4 (= process4)
            Message message6 = new Message(6, process4.getIndex(), process3.getIndex());
            process4.send(setup.getUrls()[process3.getIndex()], message6);
            
            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(300);

            List<Message> messagesProcess2 = process2.getMessages();
            Assert.assertTrue(1 == messagesProcess2.size());
            Assert.assertTrue(message2.getId() == messagesProcess2.get(0).getId());
            
            List<Message> messagesProcess3 = process3.getMessages();
            Assert.assertTrue(3 == messagesProcess3.size());
            Assert.assertTrue(message1.getId() == messagesProcess3.get(0).getId());
            Assert.assertTrue(message4.getId() == messagesProcess3.get(1).getId());
            Assert.assertTrue(message6.getId() == messagesProcess3.get(2).getId());
                        
            List<Message> messagesProcess4 = process4.getMessages();
            Assert.assertTrue(2 == messagesProcess4.size());
            Assert.assertTrue(message5.getId() == messagesProcess4.get(0).getId());
            Assert.assertTrue(message3.getId() == messagesProcess4.get(1).getId());
            
        } catch (RemoteException e){
            e.printStackTrace();
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
		}
    }
    
}
