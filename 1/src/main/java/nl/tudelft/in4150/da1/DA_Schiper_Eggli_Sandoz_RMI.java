package nl.tudelft.in4150.da1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

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
     * Receives message from a remote node. Assures that all the messages
     * will be delivered in a casual order.
     * @param message transmitted message
     * @throws RemoteException
     */
    public void receive(Message message) throws RemoteException;

}
