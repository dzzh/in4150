package nl.tudelft.in4150.da2.message;

import java.io.Serializable;

/**
 * Abstract class providing general functionality of a message to be exchanged by (remote) processes.
 */
public abstract class Message implements Serializable {

    //URL of source process
    private String srcUrl;
    
    //id of source process
    private int srcId;

    public Message(String srcUrl, int srcId) {
        this.srcUrl = srcUrl;
        this.srcId = srcId;
    }

    public int getSrcId() {
        return srcId;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    @Override
    public String toString() {
        return "Message{" +
                "srcUrl='" + srcUrl + '\'' +
                ", srcId=" + srcId +
                '}';
    }
}
