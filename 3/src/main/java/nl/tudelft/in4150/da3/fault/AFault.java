package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

public abstract class AFault {
	protected int iterationOfFailure;
	
	public AFault(int iterationOfFailure){
		this.iterationOfFailure = iterationOfFailure;
	}
	
	public abstract Order applyFaultyBehaviour(Order order, int iteration);
}
