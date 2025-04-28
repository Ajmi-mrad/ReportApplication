package Constat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServeur {
    public static void main(String[] args) {
        System.out.println("Starting accident report server...");
        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            System.out.println("Server is waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                HandleClient handleClient = new HandleClient(clientSocket);
                handleClient.start();
            }
        } catch (IOException e) {   
            e.printStackTrace();
        }
    }

}