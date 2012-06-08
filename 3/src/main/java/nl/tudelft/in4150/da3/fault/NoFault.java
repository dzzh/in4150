package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

/**
 * Implementation of a faulty behavior that does not introduce any faults thus representing a healthy process
 */
public class NoFault extends AFault {

    private static final long serialVersionUID = 1L;

	public NoFault(int iterationOfFailure) {
		super(iterationOfFailure);
	}

	@Override
	public Order applyFaultyBehavior(Order order, int iteration) {
		return order;
	}

}
