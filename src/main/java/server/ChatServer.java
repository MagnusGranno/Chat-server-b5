package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer
{
    public String[] users1 = {"Granno", "Jensen", "Hansen", "Reder"};
    private ServerSocket serverSocket;
    public ConcurrentHashMap<String, ClientHandler> allClientHandlers = new ConcurrentHashMap<>();
    private BlockingQueue<String> sendQueue = new ArrayBlockingQueue<>(8);
    public Set<String> users = allClientHandlers.keySet();


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
        String[] receivers = receiver.split(",");
        for(int i = 0; i<receivers.length; i++)
        {
            allClientHandlers.get(receivers[i]).send(message);
            System.out.println(receivers[i]);
        }
//        for(String str: receivers)
//        {
//            message += sender.getMyID() + "#" +msg;
//            allClientHandlers.get(str).send(message);
//        }
    }

    public void sendOnline()
    {

//       List<String> users = (List<String>) allClientHandlers.keySet();
       //Users indeholder fx. kurt, ole og peter. Min opgave: lave en string der indeholder ONLINE#Kurt,Ole,Peter.
        String online = "ONLINE#";
        Iterator<String> it = users.iterator();
        while(it.hasNext()){
            online += it.next() + ",";
        }
        online = online.substring(0,online.length()-1);
        for(ClientHandler ch : allClientHandlers.values()){
            ch.send(online);
        }
    }



    public void addToClientHandlers(String name, ClientHandler client)
    {
        allClientHandlers.put(name, client);
    }


    private void startServer(int port) throws IOException
    {
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
