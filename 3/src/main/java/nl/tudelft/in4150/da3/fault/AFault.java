package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

import java.io.Serializable;

/**
 * Abstraction of a faulty behavior that is used to simulate process faults.
 */
public abstract class AFault implements Serializable {

	protected int iterationOfFailure;
	
	public AFault(int iterationOfFailure){
		this.iterationOfFailure = iterationOfFailure;
	}
	
	public abstract Order applyFaultyBehavior(Order order, int iteration);
}
