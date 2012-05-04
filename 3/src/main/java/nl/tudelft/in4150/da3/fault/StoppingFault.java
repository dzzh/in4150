package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

public class StoppingFault extends AFault {

	public StoppingFault(int iterationOfFailure) {
		super(iterationOfFailure);
	}

	@Override
	public Order applyFaultyBehaviour(Order order, int iteration) {
		if(iteration >= this.iterationOfFailure)
		{
			return null;
		}
		
		return order;
	}

}
