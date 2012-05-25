package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;
import nl.tudelft.in4150.da3.Order;
import nl.tudelft.in4150.da3.message.OrderMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
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
    public void testSimple(){
        DA_Byzantine_RMI commanderProcess = (DA_Byzantine_RMI) setup.getProcesses().get(0);
        TestThread thread1 = new TestThread(commanderProcess);
        new Thread(thread1).start();
        DA_Byzantine_RMI lieutenantProcess1 = (DA_Byzantine_RMI) setup.getProcesses().get(1);
        TestThread thread2 = new TestThread(lieutenantProcess1);
        new Thread(thread2).start();
        DA_Byzantine_RMI lieutenantProcess2 = (DA_Byzantine_RMI) setup.getProcesses().get(2);
        TestThread thread3 = new TestThread(lieutenantProcess2);
        new Thread(thread3).start();
        DA_Byzantine_RMI lieutenantProcess3 = (DA_Byzantine_RMI) setup.getProcesses().get(2);
        TestThread thread4 = new TestThread(lieutenantProcess3);
        new Thread(thread4).start();

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