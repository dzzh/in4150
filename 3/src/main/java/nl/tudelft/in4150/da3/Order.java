package nl.tudelft.in4150.da3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.Map.Entry;

public enum Order {

	ATTACK,
	RETREAT;

    private final static Log LOGGER = LogFactory.getLog(Order.class);

	public static Order getDefaultOrder(){
		return Order.RETREAT;
	}
	
	public static Order getMostFrequentOrder(List<Order> orders){
        List<Order> maxOrders = new LinkedList<Order>();
		Map<Order, Integer> frequencies = new HashMap<Order, Integer>();
		for (Order o : orders){
			Integer f = frequencies.get(o);
			if (f == null){
				f = 1;
			} else {
				f++;
			}
			frequencies.put(o, f);
		}
		int max = 0;
		Order order = getDefaultOrder();
		
		Collection<Entry<Order, Integer>> fCollection = frequencies.entrySet();
		for (Entry<Order, Integer> e : fCollection){
			if (e.getValue() > max){
				max = e.getValue();
				order = e.getKey();
                maxOrders.clear();
                maxOrders.add(e.getKey());
			} else if (e.getValue() == max){
                maxOrders.add(e.getKey());
            }
		}

        if (maxOrders.size() > 1){
            LOGGER.debug("Equal number of occurrences for the following orders: " + maxOrders);
            LOGGER.debug("Decided to stay at default order.");
            order = getDefaultOrder();
        }

		return order;
	}
}
