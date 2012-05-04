package nl.tudelft.in4150.da3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Token implements Serializable {

    List<Integer> TN;
    private static boolean isInstantiated = false;

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
    public static Token instantiate(int numProcesses){
        if (!isInstantiated){
            isInstantiated = true;
            return new Token(numProcesses);
        }
        return null;
    }

    public List<Integer> getTN() {
        return TN;
    }
}
