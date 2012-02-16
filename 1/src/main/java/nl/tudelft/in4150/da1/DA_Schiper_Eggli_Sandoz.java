package nl.tudelft.in4150.da1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model of a process in a distributed system that can exchange messages
 * with other nodes. Ensures that the messages will be delivered in a casual order
 * implemented with Schiper-Eggli-Sandoz algorithm.
 */
public class DA_Schiper_Eggli_Sandoz extends UnicastRemoteObject implements DA_Schiper_Eggli_Sandoz_RMI{

    private HashMap<Integer, ArrayList<Integer>> buffer;

    protected DA_Schiper_Eggli_Sandoz() throws RemoteException {
        super();
    }

    public void receive(Message message) throws RemoteException {

    }

    public void send(Message message){

    }

    private void deliver(){

    }
}
