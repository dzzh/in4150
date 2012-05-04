package nl.tudelft.in4150.da3;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to support RMI operations for Suzuki-Kasami mutual exclusion algorithm.
 */
public interface DA_Byzantine_RMI extends Remote{

    /**
     * Index of a current process
     * @return index
     * @throws RemoteException
     */
    public int getIndex() throws RemoteException;

    /**
     * Resets the state of current process allowing to run several test cases without restart.
     * @throws RemoteException
     */
    public void reset() throws RemoteException;

}
