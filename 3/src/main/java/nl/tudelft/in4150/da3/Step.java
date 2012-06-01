package nl.tudelft.in4150.da3;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.tudelft.in4150.da3.message.OrderMessage;

/**
 * Contains information about the messages needed for a certain round of algorithm execution 
 */
public class Step {

	private static Log LOGGER = LogFactory.getLog(Step.class);
	
	//time out in ms to wait for slow messages after receiving minimum number to proceed
	public static long WAITING_TIME_OUT = 100;

	private StepState state = StepState.FORMS_POOL;
	private List<OrderMessage> messages;
	private int maxTraitors;
	private int numProcesses;
	private long startWaitingForMissedMessages = 0;
	
	public Step(int numProcesses, int maxTraitors){
		this.messages = new LinkedList<OrderMessage>();
		this.maxTraitors = maxTraitors;
		this.numProcesses = numProcesses;
	}
	
	public boolean addMessage(OrderMessage message){
		
		if (state == StepState.READY){
			return false;
		}
		
		//misbehavior prevention
		for (OrderMessage om : messages){
			if (om.getId() == message.getId()){
				return false;
			}
		}
		
		messages.add(message);
		LOGGER.debug("Added message to Step with " + maxTraitors + " traitors. Messages: " + messages.size());
		//max mumber of messages in a communication round is number of processors - traitors - commander - process itself
		if (messages.size() == numProcesses - maxTraitors - 2){
			state = StepState.WAITS_FOR_TIME_OUT;
			startWaitingForMissedMessages = System.currentTimeMillis();
		}
		
		return true;
	}
	
	/**
	 * Process is ready to be processed only if in READY state or waiting timeout for missed messages has expired
	 * @return true if the step can be processed, false otherwise
	 */
	public boolean isReady(){
		if (state == StepState.FORMS_POOL){
			return false;
		} else if (state == StepState.READY){
			return true;
		} else{
			long now = System.currentTimeMillis();
			if (now - startWaitingForMissedMessages > WAITING_TIME_OUT){
				state = StepState.READY;
				return true;
			}
			return false;
		}
	}
	
	public boolean isWaitingForMissedMessages(){
		return state == StepState.WAITS_FOR_TIME_OUT;
	}
	
	public List<OrderMessage> getMessages(){
		return messages;
	}

    public int getMaxTraitors() {
        return maxTraitors;
    }
}
