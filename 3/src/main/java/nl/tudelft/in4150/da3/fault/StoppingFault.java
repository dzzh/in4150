package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

/**
 * Implementation of a faulty behavior that never return any messages
 */
public class StoppingFault extends AFault {

    private static final long serialVersionUID = 1L;

	public StoppingFault(int iterationOfFailure) {
		super(iterationOfFailure);
	}

	@Override
	public Order applyFaultyBehavior(Order order, int iteration) {
		if(iteration >= this.iterationOfFailure)
		{
			return null;
		}
		
		return order;
	}

}
