package nl.tudelft.in4150.da3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import nl.tudelft.in4150.da3.fault.AFault;
import nl.tudelft.in4150.da3.message.AckMessage;
import nl.tudelft.in4150.da3.message.OrderMessage;

/**
 * Remote interface to support RMI operations for Lamport-Pease-Shostak algorithm.
 */
public interface DA_Byzantine_RMI extends Remote{

	/**
	 * Indicates whether the decision for this process was made
	 * @return processIsDone : Boolean
	 * @throws RemoteException
	 */
	public boolean isDone() throws RemoteException;
	
	/**
	 * Actions by process on order delivery
	 * @param message order
	 * @throws RemoteException
	 */
	public void receiveOrder(OrderMessage message) throws RemoteException;
	
	/**
	 * Actions by process on receiving a message delivery acknowledgment 
	 * @param message acknowledgment message
	 * @throws RemoteException
	 */
	public void receiveAck(AckMessage message) throws RemoteException;
	
	/**
	 * Service method to check whether the process is faulty
	 * @return faulty status
	 * @throws RemoteException
	 */
	public boolean hasFault() throws RemoteException;
	
	/**
	 * Sets the fault for the process. Is used by client while instantiating communication scheme.
	 * @param fault fault
	 * @throws RemoteException
	 */
	public void setFault(AFault fault) throws RemoteException;
	
	/**
	 * Get the fault of this process.
	 * @return AFault
	 * @throws RemoteException
	 */
	public AFault getFault() throws RemoteException;
		
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
    public void reset(int numProcesses) throws RemoteException;

    /**
     * Runs a decision process after the decision tree is built. Is invoked internally.
     * @throws RemoteException
     */
    public void decide() throws RemoteException;

}
