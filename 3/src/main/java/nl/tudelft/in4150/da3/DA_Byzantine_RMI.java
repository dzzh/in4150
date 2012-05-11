package nl.tudelft.in4150.da3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import nl.tudelft.in4150.da3.message.AckMessage;
import nl.tudelft.in4150.da3.message.OrderMessage;

/**
 * Remote interface to support RMI operations for Lamport-Pease-Shostak algorithm.
 */
public interface DA_Byzantine_RMI extends Remote{

	/**
	 * Start this process.
	 * @throws RemoteException
	 */
	public void start() throws RemoteException;
	
	/**
	 * Indicates whether or not this process is finished.
	 * @return processIsDone : Boolean
	 * @throws RemoteException
	 */
	public boolean done() throws RemoteException;
	
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
	public boolean isFaulty() throws RemoteException;
	
	/**
	 * Sets faulty status for the process. Is used by client while instantiating communication scheme.
	 * @param isFaulty
	 * @throws RemoteException
	 */
	public void setFaulty(boolean isFaulty) throws RemoteException;
		
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
