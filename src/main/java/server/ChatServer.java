package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, ClientHandler> allClientHandlers = new ConcurrentHashMap<>();
    private BlockingQueue<String> sendQueue = new ArrayBlockingQueue<>(8);

    public void addToSendQueue(String msg)
    {
        try {
            sendQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String msg){
        allClientHandlers.values().forEach((clientHandler -> {clientHandler.msgToAll(msg);}));
    }






    private void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started, listening on : "+port);


        while(true)
        {
            Socket socket = serverSocket.accept(); //Blocking call
            System.out.println("New Client connected");
        }
    }


    //Call server with arguments like this: 8080
    public static void main(String[] args) throws UnknownHostException {
        int port = 8088;


        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);

            } else {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }

    }


}
