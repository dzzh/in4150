package nl.tudelft.in4150.da2.message;

import java.util.List;

public class RequestMessage extends Message {

    private List<Integer> sequenceNumbers = null;

    public RequestMessage(int id, String srcUrl, int srcId, int destId){
        super(id, srcUrl, srcId, destId);
    }

    public List<Integer> getSequenceNumbers() {
        return sequenceNumbers;
    }

    public void setSequenceNumbers(List<Integer> sequenceNumbers) {
        this.sequenceNumbers = sequenceNumbers;
    }

    public void increaseSequenceNumber(int index) {
        sequenceNumbers.set(index, sequenceNumbers.get(index) + 1);
    }
}
