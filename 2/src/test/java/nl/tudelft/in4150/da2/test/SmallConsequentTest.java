package nl.tudelft.in4150.da2.test;

import nl.tudelft.in4150.da2.DA_Suzuki_Kasami_RMI;
import nl.tudelft.in4150.da2.Token;
import nl.tudelft.in4150.da2.message.TokenMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SmallConsequentTest{

    private TestSetup setup;

    private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }

    /**
     * Three processes start computing sequentially
     */
    @Test
    public void consequentTest(){
        DA_Suzuki_Kasami_RMI process1 = setup.getProcesses().get(0);
        TestThread thread1 = new TestThread(process1);
        DA_Suzuki_Kasami_RMI process2 = setup.getProcesses().get(1);
        TestThread thread2 = new TestThread(process2);
        DA_Suzuki_Kasami_RMI process3 = setup.getProcesses().get(2);
        TestThread thread3 = new TestThread(process3);

        try{
            process1.reset();
            process2.reset();
            process3.reset();
            new Thread(thread1).start();
            Token token = Token.instantiate(3);
            if (token != null){
                TokenMessage tm = new TokenMessage("",0,token);
                process1.receiveToken(tm);
            }
            Thread.sleep(3000);
            new Thread(thread2).start();
            Thread.sleep(3000);
            new Thread(thread3).start();

            Thread.sleep(5000);
            Assert.assertTrue(process1.isComputationFinished());
            Assert.assertTrue(process2.isComputationFinished());
            Assert.assertTrue(process3.isComputationFinished());

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
