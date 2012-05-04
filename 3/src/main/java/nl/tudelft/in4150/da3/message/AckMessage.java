package nl.tudelft.in4150.da3.message;

public class AckMessage extends Message {
	
	private int ackId;
	
	public AckMessage(int id, int sender, int receiver){
		super(id, sender, receiver);
	}

	public int getAckId() {
		return ackId;
	}

	public void setAckId(int ackId) {
		this.ackId = ackId;
	}

}
