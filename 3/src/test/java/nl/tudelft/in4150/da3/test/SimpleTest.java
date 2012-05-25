package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine;
import nl.tudelft.in4150.da3.DA_Byzantine_RMI;
import nl.tudelft.in4150.da3.Order;
import nl.tudelft.in4150.da3.message.OrderMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleTest{
    
    private TestSetup setup;

    private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }

    /**
     * Three processes start simultaneously
     */
    @Test
    public void testCase(){
        DA_Byzantine commanderProcess = (DA_Byzantine) setup.getProcesses().get(0);
        Thread thread1 = new Thread(commanderProcess);
        thread1.start();
        DA_Byzantine lieutenantProcess1 = (DA_Byzantine) setup.getProcesses().get(1);
        Thread thread2 = new Thread(lieutenantProcess1);
        thread2.start();
        DA_Byzantine lieutenantProcess2 = (DA_Byzantine) setup.getProcesses().get(2);
        Thread thread3 = new Thread(lieutenantProcess2);
        thread3.start();
        DA_Byzantine lieutenantProcess3 = (DA_Byzantine) setup.getProcesses().get(2);
        Thread thread4 = new Thread(lieutenantProcess3);
        thread4.start();

        int maxTraitors = 1;
        Order order = Order.ATTACK;        
        
        try{
            commanderProcess.reset();
            lieutenantProcess1.reset();
            lieutenantProcess2.reset();
            lieutenantProcess3.reset();
            
            // Gives new order to himself, like a root in a graph is it's own parent.
            // The already processed stays empty.
            // Both indicate that this process is the commander.
            OrderMessage message = new OrderMessage(0, commanderProcess.getIndex(), commanderProcess.getIndex());
            message.setMaxTraitors(maxTraitors);
            message.setOrder(order);
            commanderProcess.receiveOrder(message);
            
            // Start all processes and the commander last, so that everyone is able to receive a message.
            new Thread(thread2).start();
            new Thread(thread3).start();
            new Thread(thread4).start();
            new Thread(thread1).start();
            
            Thread.sleep(10000);
            Assert.assertTrue(commanderProcess.isDone());
            Assert.assertTrue(lieutenantProcess1.isDone());
            Assert.assertTrue(lieutenantProcess2.isDone());
            Assert.assertTrue(lieutenantProcess3.isDone());

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}