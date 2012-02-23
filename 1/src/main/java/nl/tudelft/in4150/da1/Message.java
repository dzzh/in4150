package nl.tudelft.in4150.da1;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Data class describing messages to be excahnged by remote processes.
 */
public class Message implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1752163046928173754L;
	int id = 0;
    Map<Integer, List<Integer>> sendBuffer;
    List<Integer> clock;

    //id of source process
    int srcId;

    //id of destination process
    int destId;

    //if above 0, message receipt will be delayed for delay ms
    int delay = 0;
    
    //timestamp of message arrival.
    private long timestamp;

    public Message(int id, int srcId, int destId) {
        this.id = id;
        this.srcId = srcId;
        this.destId = destId;
    }

    public Map<Integer, List<Integer>> getSendBuffer() {
        return sendBuffer;
    }

    public List<Integer> getClock() {
        return clock;
    }

    public int getSrcId() {
        return srcId;
    }

    public int getDelay() {
        return delay;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
    	this.timestamp = timestamp;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public void setSendBuffer(Map<Integer, List<Integer>> sendBuffer) {
        this.sendBuffer = sendBuffer;
    }

    public void setClock(List<Integer> clock) {
        this.clock = clock;
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
