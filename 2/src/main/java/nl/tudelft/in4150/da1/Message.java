package nl.tudelft.in4150.da1;

import java.io.Serializable;
import java.util.List;

/**
 * Data class describing messages to be excahnged by remote processes.
 */
public class Message implements Serializable {

	public enum Type {
	    REQUEST, TOKEN
	}
	
	private static final long serialVersionUID = 418628091965111177L;
	
	// message id.
	private int id;
	
	// message type.
	private Type type;
	
	// for every process the sequence number of the last request that was granted.
    private List<Integer> sequenceNumbers;
    
    private String srcURL;

    //id of source process
    private int srcId;

    //id of destination process
    private int destId;

    //if above 0, message receipt will be delayed for delay ms
    private int delay = 0;
    
    //timestamp of message arrival.
    private long timestamp;

    public Message(int id, Type type, String srcURL, int srcId, int destId) {
        this.id = id;
        this.type = type;
        this.srcURL = srcURL;
        this.srcId = srcId;
        this.destId = destId;
    }

    public List<Integer> getSequenceNumbers() {
        return sequenceNumbers;
    }
    
    public void setSequenceNumbers(List<Integer> sequenceNumbers) {
        this.sequenceNumbers = sequenceNumbers;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
    	this.timestamp = timestamp;
    }

    public int getDelay() {
        return delay;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public String getSrcURL(){
    	return this.srcURL;
    }
    
    public int getSrcId() {
        return srcId;
    }

    public int getId() {
        return id;
    }

    public Type getType()
    {
    	return this.type;
    }
    
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", srcId=" + srcId +
                ", destId=" + destId +
                ", delay=" + delay +
                '}';
    }
}
