package Constat.ServerRmi.Server;

import Constat.ClientRmi.Chat.ChatRemote;
import Constat.ClientRmi.Chat.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatImplementation extends UnicastRemoteObject implements ChatRemote {
    private final List<Message> chatList;

    protected ChatImplementation() throws RemoteException {
        //super();
        // i use Collections.synchronizedList to make the list thread safe for example it hapen a data corruption
        // when two thread try to add a message at the same time
        this.chatList = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public synchronized void addMsg(Message msg) throws RemoteException {
        // Every add() is protected by mutex to avoid data corruption
        chatList.add(msg);
        System.out.println("Message received: " + msg);
    }

    @Override
    public synchronized ArrayList<Message> getListMsg() throws RemoteException {
        return new ArrayList<>(chatList);
    }
}