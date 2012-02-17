package nl.tudelft.in4150.da1;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Message implements Serializable {

    int id = 0;
    Map<Integer, List<Integer>> sendBuffer;
    List<Integer> clock;
    String value;
    int srcId;
    int destId;
    int delay = 0;

    public Message(String value, int srcId, int destId) {
        this.value = value;
        this.srcId = srcId;
        this.destId = destId;
        delay = 0;
    }

    public Map<Integer, List<Integer>> getSendBuffer() {
        return sendBuffer;
    }

    public List<Integer> getClock() {
        return clock;
    }

    public String getValue() {
        return value;
    }

    public int getSrcId() {
        return srcId;
    }

    public int getDestId() {
        return destId;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSendBuffer(Map<Integer, List<Integer>> sendBuffer) {
        this.sendBuffer = sendBuffer;
    }

    public void setClock(List<Integer> clock) {
        this.clock = clock;
    }
}
