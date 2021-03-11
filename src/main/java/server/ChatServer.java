package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer
{

    //Jensen
    public ArrayList<String> users = new ArrayList<>(4);
    private ServerSocket serverSocket;
    public ConcurrentHashMap<String, ClientHandler> allClientHandlers = new ConcurrentHashMap<>();
    private BlockingQueue<String> sendQueue = new ArrayBlockingQueue<>(8);

    public void addToSendQueue(String msg)
    {
        try
        {
            sendQueue.put(msg);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void sendToAll(String msg)
    {
        allClientHandlers.values().forEach((clientHandler ->
        {
            clientHandler.msgToAll(msg);
        }));
    }

    public void sendSpecific(ClientHandler sender, String receiver, String msg)
    {
        String message = "MESSAGE#" +sender.getMyID()+ "#" +msg;
        allClientHandlers.get(receiver).send(message);
    }

    public void sendOnline()
    {
        String online = "ONLINE#";
        for(ClientHandler ch : allClientHandlers.values()){
            online += ch.getMyID() + ",";
        }
        online = online.substring(0,online.length()-1);
        addToSendQueue(online);
    }

    public void addToClientHandlers(String name, ClientHandler client)
    {
        allClientHandlers.put(name, client);
    }

    //Hansen
    private void startServer(int port) throws IOException
    {
        users.add("Granno");
        users.add("Reder");
        users.add("Hansen");
        users.add("Jensen");
        serverSocket = new ServerSocket(port);
        System.out.println("Server started, listening on : " + port);

        while (true)
        {
            System.out.println("Waiting for a client");
            Socket socket = serverSocket.accept(); //Blocking call
            System.out.println("New Client connected");

            ClientHandler clientHandler = new ClientHandler(socket, this);
            Thread t = new Thread(clientHandler);
            t.start();

            SendToClients stc = new SendToClients(this, sendQueue);
            Thread t1 = new Thread(stc);
            t1.start();
        }
    }


    //Call server with arguments like this: 8080
    public static void main(String[] args) throws IOException
    {
        int port = 8080;
        try
        {
            if (args.length == 1)
            {
                port = Integer.parseInt(args[0]);
            }
            else
            {
                port = 8080;
            }
        } catch (NumberFormatException ne)
        {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }
        new ChatServer().startServer(port);
    }
}
