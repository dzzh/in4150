package nl.tudelft.in4150.da3.fault;

import nl.tudelft.in4150.da3.Order;

/**
 * Implementation of a faulty behavior that sometimes does not return any orders
 */
public class RandomSendFault extends AFault {
    private double failureProbability;
    private static final long serialVersionUID = 1L;

    public RandomSendFault(int iterationOfFailure, double failureProbability) {
        super(iterationOfFailure);
        this.failureProbability = failureProbability;
    }

    @Override
    public Order applyFaultyBehavior(Order order, int iteration) {
        if (iteration >= this.iterationOfFailure && Math.random() < this.failureProbability) {
            return null;
        }

        return order;
    }

}
