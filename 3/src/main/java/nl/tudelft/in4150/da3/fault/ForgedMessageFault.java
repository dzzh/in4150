package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

/**
 * Implementation of a faulty process behavior that reverses the order with a given probability
 */
public class ForgedMessageFault extends AFault {

    private static final long serialVersionUID = 1L;

	private double forgedMessageProbability;

	public ForgedMessageFault(int iterationOfFailure, double forgedMessageProbability) {
		super(iterationOfFailure);
		this.forgedMessageProbability = forgedMessageProbability;
	}

	@Override
	public Order applyFaultyBehavior(Order order, int iteration) {
		if(iteration >= this.iterationOfFailure && Math.random() < this.forgedMessageProbability)
		{
			switch(order){
				case ATTACK:
					return Order.RETREAT;
				case RETREAT:
					return Order.ATTACK;
				default:
					return null;
			}			
		}
		
		return order;
	}

}
