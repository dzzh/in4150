package nl.tudelft.in4150.da1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

/**
 * Remote interface to support RMI operations for Schiper-Eggli-Sandoz casual ordering
 * in a dstributed system.
 */
public interface DA_Schiper_Eggli_Sandoz_RMI extends Remote{

    /**
     * Sends a message to the (remote) destination process
     * @param url location of destination process
     * @param message message to send
     * @throws RemoteException
     */
    public void send(String url, Message message) throws RemoteException;
    
    /**
     * Receives message from a (remote) process. All the messages
     * are delivered in a casual order.
     * @param message transmitted message
     * @throws RemoteException
     */
    public void receive(Message message) throws RemoteException;

    /**
     * Index of a current process
     * @return index
     * @throws RemoteException
     */
    public int getIndex() throws RemoteException;
    
    /**
     * Messages so far received by the process.
     * @return messages
     * @throws RemoteException
     */
    public LinkedList<Message> getMessages() throws RemoteException;

    /**
     * Resets the state of current process allowing to run several test cases without restart.
     * @throws RemoteException
     */
    public void reset() throws RemoteException;

}
