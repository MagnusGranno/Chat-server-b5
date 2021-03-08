package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter pw;
    ChatServer chatServer;

    Scanner getName = new Scanner(System.in);

    private String myID = "";

    private ArrayList<String> connectedNames = new ArrayList<>();

    public String getMyID() {
        return myID;
    }
    public ClientHandler(Socket socket, ChatServer chatServer)
    {
        this.socket = socket;
        this.chatServer = chatServer;

    }


//    public String sendOnline()
//    {
//        String currentlyOnline = "ONLINE#";
//        for(ClientHandler name : chatServer.getAllClientHandlers())
//        {
//            currentlyOnline += name +",";
//
//
//        }
//        return currentlyOnline;
//    }

    public void msgToAll(String msg) {
        pw.println(msg);
    }


    private boolean handleCommand(String msg, PrintWriter pw, Scanner scanner)
    {
        String[] parts = msg.split("#");
        if(parts.length == 1)
        {
            if(parts[0].equals("CLOSE")){
                pw.println("CLOSE#");
                return false;
            }

        } else if(parts.length == 2){
            String token = parts[0];
            String argument = parts[1];
            switch (token){
                case "CONNECT" :
                    myID = argument;
                    connectedNames.add(myID);
                    chatServer.addToSendQueue(chatServer.online());

                    break;
                case "SEND" :

                    break;



            }
        } else if(parts.length == 3)
        {

        }
        return true;
    }

    private void handleClient() throws IOException {

        pw = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(socket.getInputStream());
        pw.println("Please connect before continuing - example: CONNECT#John");

        try {
            String message = "";
            boolean keepRunning = true;
            while(keepRunning) {
                message = scanner.nextLine(); //Blocking call
                keepRunning = handleCommand(message, pw, scanner);
            }
        } catch (Exception e) {
            System.out.println("");
            e.printStackTrace();
        }
        pw.println("Connection closed");
        socket.close();
    }


    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
