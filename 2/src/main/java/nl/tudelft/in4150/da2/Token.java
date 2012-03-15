package nl.tudelft.in4150.da2;

import java.util.ArrayList;
import java.util.List;

public class Token {

    List<Integer> TN;
    private Token instance = null;
    boolean isInstantiated = false;

    /**
     * The constructor is made private as Token is a singleton created only once for the whole system.
     * @param numProcesses - number of processes in a system
     */
    private Token(int numProcesses){
        TN = new ArrayList<Integer>();
        for (int i = 0; i < numProcesses; i++){
            TN.add(0);
        }

    }

    /**
     * Instantiates token if executes for the first time
     * @return token after first execution, null otherwise
     */
    public Token instantiate(int numProcesses){
        if (!isInstantiated){
            instance = new Token(numProcesses);
            isInstantiated = true;
            return instance;
        }

        return null;
    }

    public List<Integer> getTN() {
        return TN;
    }

    public void setTN(List<Integer> TN) {
        this.TN = TN;
    }
}
