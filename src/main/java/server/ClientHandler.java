package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter pw;
    ChatServer chatServer;

    static int index = 1;
    private String myID = "name: ";

    public String getMyID() {
        return myID;
    }
    public ClientHandler(Socket socket, ChatServer chatServer)
    {
        this.socket = socket;
        this.myID += index;
        index++;
        this.chatServer = chatServer;
    }


    public void msgToAll(String msg) {
        pw.println("msg" +msg);
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
        pw.println("Du er connected");

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
