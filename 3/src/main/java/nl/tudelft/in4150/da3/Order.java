package nl.tudelft.in4150.da3;

public enum Order {

	ATTACK,
	WAIT;
	
	public static Order getDefaultOrder(){
		return Order.WAIT;
	}
	
}
