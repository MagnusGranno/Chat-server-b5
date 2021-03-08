package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    public String[] users = {"Granno", "Jensen", "Hansen", "Reder"};

    public HashMap<String, Integer> ClientAddress = new HashMap<>();



    private ServerSocket serverSocket;
    public ConcurrentHashMap<String, ClientHandler> allClientHandlers = new ConcurrentHashMap<>();
    private BlockingQueue<String> sendQueue = new ArrayBlockingQueue<>(8);




    public void addToSendQueue(String msg)
    {


        try {
            sendQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    public void sendToAll(String msg)
    {
        allClientHandlers.values().forEach((clientHandler -> {clientHandler.msgToAll(msg);}));

    }



    public ArrayList<String> connectedNames = new ArrayList<>(4);

    public String sendOnline()
    {
        String currentlyOnline = "";
        int listLength = connectedNames.size();
        switch(listLength) {
            case 1:
                currentlyOnline = "ONLINE#" + connectedNames.get(0);
                break;
            case 2:
                currentlyOnline = "ONLINE#" + connectedNames.get(0) + "," +connectedNames.get(1);
                break;
            case 3:
                currentlyOnline = "ONLINE#" + connectedNames.get(0) + "," +connectedNames.get(1)+ "," +connectedNames.get(2);
                break;
            case 4:
                currentlyOnline = "ONLINE#" + connectedNames.get(0) + "," +connectedNames.get(1) + "," + connectedNames.get(2) + "," + connectedNames.get(3);
                break;
        }
        return currentlyOnline;
    }
//    public String connected()
//    {
//        String online = "ONLINE#";
//        for(String name : connectedNames)
//        {
//            online += name;
//        }
//        return online;
//    }




    private void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started, listening on : "+port);


        while(true)
        {
            System.out.println("Waiting for a client");
            Socket socket = serverSocket.accept(); //Blocking call
            System.out.println("New Client connected");

            ClientHandler clientHandler = new ClientHandler(socket, this);
            allClientHandlers.put(clientHandler.getMyID(), clientHandler);
            SendToClients stc = new SendToClients(this,sendQueue);

            Thread t1 = new Thread(stc);
            t1.start();

            Thread t = new Thread(clientHandler);
            t.start();

        }
    }


    //Call server with arguments like this: 8080
    public static void main(String[] args) throws IOException {
        int port = 8080;


        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);

            } else {
                port =8080;
                //throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }
        new ChatServer().startServer(port);

    }


}
