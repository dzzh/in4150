package nl.tudelft.in4150.da2;

import nl.tudelft.in4150.da2.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to support RMI operations for Schiper-Eggli-Sandoz casual ordering
 * in a dstributed system.
 */
public interface DA_Suzuki_Kasami_RMI extends Remote{

    /**
     * Sends a message to the (remote) destination process
     * @param url location of destination process
     * @param message message to send
     * @throws RemoteException
     */
    //public void send(String url, Message message) throws RemoteException;
	
	/**
	 * Does computations requiring access to the CS.
	 */
	public void compute() throws RemoteException;
    
    /**
     * Receives request message from a (remote) process.
     * @param message transmitted message
     * @throws RemoteException
     */
    public void receiveRequest(Message message) throws RemoteException;

    public void receiveToken(Message message) throws RemoteException;
    
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
