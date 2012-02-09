package nl.tudelft.in4150.da1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DA_Schiper_Eggli_Sandoz_RMI extends Remote{

    public void receive(Message message) throws RemoteException;

}
