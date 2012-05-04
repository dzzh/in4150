package nl.tudelft.in4150.da3.message;

import java.util.LinkedList;
import java.util.List;

import nl.tudelft.in4150.da3.Order;

public class OrderMessage extends Message{
	
	private Order order;
	int maxTraitors;
	List<Integer> alreadyProcessed;
	
	public OrderMessage(int id, int sender, int receiver){
		super(id, sender, receiver);
		alreadyProcessed = new LinkedList<Integer>();
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getMaxTraitors() {
		return maxTraitors;
	}

	public void setMaxTraitors(int maxTraitors) {
		this.maxTraitors = maxTraitors;
	}

	public List<Integer> getAlreadyProcessed() {
		return alreadyProcessed;
	}

	public void setAlreadyProcessed(List<Integer> alreadyProcessed) {
		this.alreadyProcessed = alreadyProcessed;
	}
}
