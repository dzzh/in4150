package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;
import nl.tudelft.in4150.da3.Order;
import nl.tudelft.in4150.da3.fault.ForgedMessageFault;
import nl.tudelft.in4150.da3.message.OrderMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SevenGeneralsThreeForgedMessageFaultLieutenants {
	private TestSetup setup;

    //private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }

    @Test
    public void test(){
        int numProcesses = 7;

        DA_Byzantine_RMI commanderProcess = setup.getProcesses().get(0);
        DA_Byzantine_RMI lieutenantProcess1 = setup.getProcesses().get(1);
        DA_Byzantine_RMI lieutenantProcess2 = setup.getProcesses().get(2);
        DA_Byzantine_RMI lieutenantProcess3 = setup.getProcesses().get(3);
        DA_Byzantine_RMI lieutenantProcess4 = setup.getProcesses().get(4);
        DA_Byzantine_RMI lieutenantProcess5 = setup.getProcesses().get(5);
        DA_Byzantine_RMI lieutenantProcess6 = setup.getProcesses().get(6);

        int maxTraitors = 3;
        Order order = Order.ATTACK;        
        
        try{
            commanderProcess.reset(numProcesses);
            lieutenantProcess1.reset(numProcesses);
            lieutenantProcess2.reset(numProcesses);
            lieutenantProcess3.reset(numProcesses);
            lieutenantProcess4.reset(numProcesses);
            lieutenantProcess5.reset(numProcesses);
            lieutenantProcess6.reset(numProcesses);
            lieutenantProcess1.setFault(new ForgedMessageFault(0, 1));
            lieutenantProcess2.setFault(new ForgedMessageFault(0, 1));
            lieutenantProcess3.setFault(new ForgedMessageFault(0, 1));
            
            // Gives new order to himself, like a root in a graph is it's own parent.
            // The already processed stays empty.
            // Both indicate that this process is the commander.
            OrderMessage message = new OrderMessage(0, commanderProcess.getIndex(), commanderProcess.getIndex());
            message.setCurrentMaxTraitors(maxTraitors);
            message.setTotalTraitors(maxTraitors);
            message.setOrder(order);
            commanderProcess.receiveOrder(message);
            
            Thread.sleep(10000);
            Assert.assertTrue(commanderProcess.isDone());
            Assert.assertTrue(lieutenantProcess1.isDone());
            Assert.assertTrue(lieutenantProcess2.isDone());
            Assert.assertTrue(lieutenantProcess3.isDone());
            Assert.assertTrue(lieutenantProcess4.isDone());
            Assert.assertTrue(lieutenantProcess5.isDone());
            Assert.assertTrue(lieutenantProcess6.isDone());
            
            Assert.assertEquals(order, commanderProcess.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess1.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess2.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess3.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess4.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess5.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess6.getFinalOrder());

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
