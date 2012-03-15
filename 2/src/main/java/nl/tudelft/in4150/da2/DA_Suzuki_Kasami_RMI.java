package nl.tudelft.in4150.da2;

import nl.tudelft.in4150.da2.message.RequestMessage;
import nl.tudelft.in4150.da2.message.TokenMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to support RMI operations for Suzuki-Kasami mutual exclusion algorithm.
 */
public interface DA_Suzuki_Kasami_RMI extends Remote{

	/**
	 * Does computations requiring access to the CS.
	 */
	public void compute() throws RemoteException;
    
    /**
     * Receives request message from a (remote) process.
     * @param requestMessage transmitted message
     * @throws RemoteException
     */
    public void receiveRequest(RequestMessage requestMessage) throws RemoteException;

    /**
     * Receives token from a (remote) process
     * @param tokenMessage message with token
     * @throws RemoteException
     */
    public void receiveToken(TokenMessage tokenMessage) throws RemoteException;
    
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
