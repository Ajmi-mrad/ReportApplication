package Constat.ClientRmi.Chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatRemote extends Remote {
    public void addMsg(Message msg) throws RemoteException;
    public ArrayList<Message> getListMsg() throws  RemoteException;

}

