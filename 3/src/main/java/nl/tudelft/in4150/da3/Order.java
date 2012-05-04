package nl.tudelft.in4150.da3;

public enum Order {

	ATTACK,
	RETREAT;
	
	public static Order getDefaultOrder(){
		return Order.RETREAT;
	}	
}
