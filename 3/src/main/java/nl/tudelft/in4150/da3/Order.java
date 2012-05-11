package nl.tudelft.in4150.da3;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public enum Order {

	ATTACK,
	RETREAT;
	
	public static Order getDefaultOrder(){
		return Order.RETREAT;
	}
	
	public static Order getMostFrequentOrder(List<Order> orders){
		Map<Order, Integer> frequencies = new HashMap<Order, Integer>();
		for (Order o : orders){
			Integer f = frequencies.get(o);
			if (f == null){
				f = new Integer(1);
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
			}
		}
		
		return order;
	}
}
