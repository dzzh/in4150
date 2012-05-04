package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

public class NoFault extends AFault {

	public NoFault(int iterationOfFailure) {
		super(iterationOfFailure);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Order applyFaultyBehaviour(Order order, int iteration) {
		return order;
	}

}
