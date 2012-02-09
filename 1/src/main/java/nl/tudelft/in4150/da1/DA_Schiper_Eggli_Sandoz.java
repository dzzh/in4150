package nl.tudelft.in4150.da1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DA_Schiper_Eggli_Sandoz extends UnicastRemoteObject implements DA_Schiper_Eggli_Sandoz_RMI{

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
