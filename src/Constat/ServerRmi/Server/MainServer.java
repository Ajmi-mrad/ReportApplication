package Constat.ServerRmi.Server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MainServer {
    public static void main(String[] args) {
        try {
            System.out.println("Starting chat server...");

            // Create RMI registry on port 9003
            LocateRegistry.createRegistry(9003);
            System.out.println("RMI registry created on port 9003");

            // Create and bind chat service
            ChatImplementation chatService = new ChatImplementation();
            String url = "rmi://localhost:9003/chatapp";
            Naming.rebind(url, chatService);

            System.out.println("Chat service bound to: " + url);
            System.out.println("Server is ready and waiting for connections...");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}