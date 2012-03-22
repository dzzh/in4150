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

public class LargeMixedTest{

    private TestSetup setup;

    private final static Log LOGGER = LogFactory.getLog(SimpleTest.class);

    @Before
    public void init(){
        setup = new TestSetup();
        setup.init();
    }

    /**
     * Seven processes start computing simultaneously, then three more are added
     */
    @Test
    @Ignore
    public void testCase(){
        DA_Suzuki_Kasami_RMI process1 = setup.getProcesses().get(0);
        TestThread thread1 = new TestThread(process1);
        DA_Suzuki_Kasami_RMI process2 = setup.getProcesses().get(1);
        TestThread thread2 = new TestThread(process2);
        DA_Suzuki_Kasami_RMI process3 = setup.getProcesses().get(2);
        TestThread thread3 = new TestThread(process3);
        DA_Suzuki_Kasami_RMI process4 = setup.getProcesses().get(3);
        TestThread thread4 = new TestThread(process4);
        DA_Suzuki_Kasami_RMI process5 = setup.getProcesses().get(4);
        TestThread thread5 = new TestThread(process5);
        DA_Suzuki_Kasami_RMI process6 = setup.getProcesses().get(5);
        TestThread thread6 = new TestThread(process6);
        DA_Suzuki_Kasami_RMI process7 = setup.getProcesses().get(6);
        TestThread thread7 = new TestThread(process7);
        DA_Suzuki_Kasami_RMI process8 = setup.getProcesses().get(7);
        TestThread thread8 = new TestThread(process8);
        DA_Suzuki_Kasami_RMI process9 = setup.getProcesses().get(8);
        TestThread thread9 = new TestThread(process9);
        DA_Suzuki_Kasami_RMI process10 = setup.getProcesses().get(9);
        TestThread thread10 = new TestThread(process10);

        try{
            process1.reset();
            process2.reset();
            process3.reset();
            process4.reset();
            process5.reset();
            process6.reset();
            process7.reset();
            process8.reset();
            process9.reset();
            process10.reset();
            new Thread(thread1).start();
            Token token = Token.instantiate(10);
            if (token != null){
                TokenMessage tm = new TokenMessage("",0,token);
                process1.receiveToken(tm);
            }
            new Thread(thread2).start();
            new Thread(thread3).start();
            new Thread(thread4).start();
            new Thread(thread5).start();
            new Thread(thread6).start();
            new Thread(thread7).start();
            Thread.sleep(15000);
            new Thread(thread8).start();
            new Thread(thread9).start();
            new Thread(thread10).start();
            Thread.sleep(10000);

            Assert.assertTrue(process1.isComputationFinished());
            Assert.assertTrue(process2.isComputationFinished());
            Assert.assertTrue(process3.isComputationFinished());
            Assert.assertTrue(process4.isComputationFinished());
            Assert.assertTrue(process5.isComputationFinished());
            Assert.assertTrue(process6.isComputationFinished());
            Assert.assertTrue(process7.isComputationFinished());
            Assert.assertTrue(process8.isComputationFinished());
            Assert.assertTrue(process9.isComputationFinished());
            Assert.assertTrue(process10.isComputationFinished());

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

}
