package nl.tudelft.in4150.da3.test;

import nl.tudelft.in4150.da3.DA_Byzantine_RMI;
import nl.tudelft.in4150.da3.Order;
import nl.tudelft.in4150.da3.fault.RandomSendFault;
import nl.tudelft.in4150.da3.message.OrderMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TenGeneralsThreeRandomSendFaultLieutenants {
	private TestSetup setup;

    //private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }

    @Test
    public void test(){
        int numProcesses = 10;

        DA_Byzantine_RMI commanderProcess = setup.getProcesses().get(0);
        DA_Byzantine_RMI lieutenantProcess1 = setup.getProcesses().get(1);
        DA_Byzantine_RMI lieutenantProcess2 = setup.getProcesses().get(2);
        DA_Byzantine_RMI lieutenantProcess3 = setup.getProcesses().get(3);
        DA_Byzantine_RMI lieutenantProcess4 = setup.getProcesses().get(4);
        DA_Byzantine_RMI lieutenantProcess5 = setup.getProcesses().get(5);
        DA_Byzantine_RMI lieutenantProcess6 = setup.getProcesses().get(6);
        DA_Byzantine_RMI lieutenantProcess7 = setup.getProcesses().get(7);
        DA_Byzantine_RMI lieutenantProcess8 = setup.getProcesses().get(8);
        DA_Byzantine_RMI lieutenantProcess9 = setup.getProcesses().get(9);

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
            lieutenantProcess7.reset(numProcesses);
            lieutenantProcess8.reset(numProcesses);
            lieutenantProcess9.reset(numProcesses);
            lieutenantProcess1.setFault(new RandomSendFault(0, 0.5));
            lieutenantProcess2.setFault(new RandomSendFault(0, 0.5));
            lieutenantProcess3.setFault(new RandomSendFault(0, 0.5));
            
            // Gives new order to himself, like a root in a graph is it's own parent.
            // The already processed stays empty.
            // Both indicate that this process is the commander.
            OrderMessage message = new OrderMessage(0, commanderProcess.getIndex(), commanderProcess.getIndex());
            message.setCurrentMaxTraitors(maxTraitors);
            message.setTotalTraitors(maxTraitors);
            message.setOrder(order);
            commanderProcess.receiveOrder(message);
            
            Thread.sleep(15000);
            
            int totalNumberOfReceivedMessages = commanderProcess.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess1.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess2.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess3.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess4.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess5.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess6.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess7.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess8.getNumberOfReceivedMessages();
            totalNumberOfReceivedMessages += lieutenantProcess9.getNumberOfReceivedMessages();
            
            System.out.println("Total number of messages send: " + totalNumberOfReceivedMessages);
            
            Assert.assertTrue(commanderProcess.isDone());
            Assert.assertTrue(lieutenantProcess1.isDone());
            Assert.assertTrue(lieutenantProcess2.isDone());
            Assert.assertTrue(lieutenantProcess3.isDone());
            Assert.assertTrue(lieutenantProcess4.isDone());
            Assert.assertTrue(lieutenantProcess5.isDone());
            Assert.assertTrue(lieutenantProcess6.isDone());
            Assert.assertTrue(lieutenantProcess7.isDone());
            Assert.assertTrue(lieutenantProcess8.isDone());
            Assert.assertTrue(lieutenantProcess9.isDone());
            
            Assert.assertEquals(order, commanderProcess.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess1.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess2.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess3.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess4.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess5.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess6.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess6.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess8.getFinalOrder());
            Assert.assertEquals(order, lieutenantProcess9.getFinalOrder());

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
