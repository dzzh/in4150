package nl.tudelft.in4150.da3.message;

import java.util.LinkedList;
import java.util.List;

import nl.tudelft.in4150.da3.Order;

public class OrderMessage extends Message{
	
	private static final long serialVersionUID = -5697651885486403504L;
	private Order order;
	int currentMaxTraitors;
    int totalTraitors;
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

	public int getCurrentMaxTraitors() {
		return currentMaxTraitors;
	}

	public void setCurrentMaxTraitors(int currentMaxTraitors) {
		this.currentMaxTraitors = currentMaxTraitors;
	}

    public int getTotalTraitors() {
        return totalTraitors;
    }

    public void setTotalTraitors(int totalTraitors) {
        this.totalTraitors = totalTraitors;
    }

    public List<Integer> getAlreadyProcessed() {
		return alreadyProcessed;
	}

	public void setAlreadyProcessed(List<Integer> alreadyProcessed) {
		this.alreadyProcessed = alreadyProcessed;
	}
	
	@Override
	public String toString() {
		return getSender() + "->" + getReceiver() + ": OrderMessage [order=" + order + ", currentMaxTraitors=" + currentMaxTraitors
				+ ", alreadyProcessed=" + alreadyProcessed + "]";
	}
	
	/*
	public String key()
	{
		String key = "|";
		for (int id : this.alreadyProcessed)
		{
			key+=id+"|";
		}
		return key;
	}
	*/
}
