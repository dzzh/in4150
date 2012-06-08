package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;
import nl.tudelft.in4150.da3.Order;
import nl.tudelft.in4150.da3.message.OrderMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ThreeGeneralsTest {
    
    private TestSetup setup;

//    private final static Log LOGGER = LogFactory.getLog(ThreeGeneralsTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }

    @Test
    public void testThreeGenerals(){
        DA_Byzantine_RMI commanderProcess = setup.getProcesses().get(0);
        TestThread thread1 = new TestThread(commanderProcess);
        DA_Byzantine_RMI lieutenantProcess1 = setup.getProcesses().get(1);
        TestThread thread2 = new TestThread(lieutenantProcess1);
        DA_Byzantine_RMI lieutenantProcess2 = setup.getProcesses().get(2);
        TestThread thread3 = new TestThread(lieutenantProcess2);

        int maxTraitors = 1;
        Order order = Order.ATTACK;        
        
        try{
            commanderProcess.reset();
            lieutenantProcess1.reset();
            lieutenantProcess2.reset();

            //Assign order to the root process to initiate algorithm execution
            OrderMessage message = new OrderMessage(0, commanderProcess.getIndex(), commanderProcess.getIndex());
            message.setCurrentMaxTraitors(maxTraitors);
            message.setTotalTraitors(maxTraitors);
            message.setOrder(order);
            commanderProcess.receiveOrder(message);
            
            // Start all processes and the commander last, so that everyone is able to receive a message.
            new Thread(thread2).start();
            new Thread(thread3).start();
            new Thread(thread1).start();
            
            Thread.sleep(10000);
            Assert.assertTrue(commanderProcess.isDone());
            Assert.assertTrue(lieutenantProcess1.isDone());
            Assert.assertTrue(lieutenantProcess2.isDone());

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}