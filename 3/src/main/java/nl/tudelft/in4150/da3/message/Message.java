package nl.tudelft.in4150.da3.message;

public class Message {

	private int id;
	private int sender;
	private int receiver;
	private long timestamp;
	
	public Message(int id, int sender, int receiver) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		timestamp = System.currentTimeMillis();
	}

	public int getId() {
		return id;
	}

	public int getSender() {
		return sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public long getTimestamp() {
		return timestamp;
	}

	
}
