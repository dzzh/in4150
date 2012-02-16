package nl.tudelft.in4150.da1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to support RMI operations for Schiper-Eggli-Sandoz casual ordering
 * in a dstributed system.
 */
public interface DA_Schiper_Eggli_Sandoz_RMI extends Remote{

    /**
     * Receives message from a remote node. Assures that all the messages
     * will be delivered in a casual order.
     * @param message transmitted message
     * @throws RemoteException
     */
    public void receive(Message message) throws RemoteException;

}
