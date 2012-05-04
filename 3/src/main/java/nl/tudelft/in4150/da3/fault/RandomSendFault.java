package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

public class RandomSendFault extends AFault {
	private double failureProbability;

	public RandomSendFault(int iterationOfFailure, double failureProbability) {
		super(iterationOfFailure);
		this.failureProbability = failureProbability;
	}

	@Override
	public Order applyFaultyBehaviour(Order order, int iteration) {
		if(iteration >= this.iterationOfFailure && Math.random() < this.failureProbability)
		{
			return null;			
		}
		
		return order;
	}

}
