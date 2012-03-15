package nl.tudelft.in4150.da2.message;

import java.io.Serializable;

/**
 * Abstract class providing general functionality of a message to be exchanged by (remote) processes.
 */
public abstract class Message implements Serializable {
	
	// message id.
	private int id;

    private String srcURL;

    //id of source process
    private int srcId;

    //id of destination process
    private int destId;

    public Message(int id, String srcURL, int srcId, int destId) {
        this.id = id;
        this.srcURL = srcURL;
        this.srcId = srcId;
        this.destId = destId;
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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", srcId=" + srcId +
                ", destId=" + destId +
                '}';
    }
}
