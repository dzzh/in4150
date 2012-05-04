package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

public class ForgedMessageFault extends AFault {
	private double forgedMessageProbability;

	public ForgedMessageFault(int iterationOfFailure, double forgedMessageProbability) {
		super(iterationOfFailure);
		this.forgedMessageProbability = forgedMessageProbability;
	}

	@Override
	public Order applyFaultyBehaviour(Order order, int iteration) {
		if(iteration >= this.iterationOfFailure && Math.random() < this.forgedMessageProbability)
		{
			switch(order){
				case ATTACK:
					return Order.WAIT;
				case WAIT:
					return Order.ATTACK;
				default:
					return null;
			}			
		}
		
		return order;
	}

}
