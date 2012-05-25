/**
 * 
 */
package nl.tudelft.in4150.da3;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OrderSet extends Observable implements Observer 
{
	private static Log LOGGER = LogFactory.getLog(OrderSet.class);
	
	Map<List<Integer>, Order> orderset;
	public int size;

	/**
	 * Construct OrderSet instance and add values of commander.
	 * @param size number of orders in the set
	 * @param key initial key (from commander)
	 * @param order initial order (form commander)
	 */
	public OrderSet(int size, List<Integer> key, Order order) 
	{
		this.orderset = new HashMap<List<Integer>, Order>();
		this.orderset.put(key, order);
		this.size = size;
	}
	
	/**
	 * Add values of a lieutenant, when the size is reached notify the observers with the outcome of the majority vote of this set and the corresponding key.
	 * @param key (from lieutenant)
	 * @param order (from lieutenant)
	 */
    public void add(List<Integer> key, Order order)
    {
    	this.orderset.put(key, order);
    	
    	if (this.orderset.size() == this.size)
    	{
    		LOGGER.debug("Observers notified.");
    		// The key from the commander.
    		List<Integer> initialKey = this.orderset.keySet().iterator().next();
    		AbstractMap.SimpleEntry<List<Integer>, Order>  se = new AbstractMap.SimpleEntry<List<Integer>, Order>(initialKey, this.majority());
    		
    		this.setChanged();
    		this.notifyObservers( se );
    	}
    }
    
    /**
     * Determine which order occurs most in the set or orders.
     * @return the most occurring order
     */
    private Order majority()
    {
    	return Order.getMostFrequentOrder(new ArrayList<Order>(this.orderset.values()));
    }

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
    /**
     * Update observer with the outcome of the majority vote of the set of the Observable and the corresponding key.
     */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		AbstractMap.SimpleEntry<List<Integer>, Order> se;
		if (arg1 instanceof AbstractMap.SimpleEntry<?,?>)
		{
			try
			{
				se = (AbstractMap.SimpleEntry<List<Integer> ,Order>) arg1;
				this.add(se.getKey(), se.getValue());
			}
			catch (Exception e)
			{
				// do something useful on exception.
			}
		}		
	}
}