package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientHandler implements Runnable
{
    private Socket socket;
    private PrintWriter pw;
    ChatServer chatServer;
    static int index = 1;
    private String myID = "name: ";


    public String getMyID()
    {
        return myID;
    }

    public ClientHandler(Socket socket, ChatServer chatServer) throws IOException
    {
        this.socket = socket;
        this.chatServer = chatServer;
        this.myID += index;
        index++;

    }


    public void msgToAll(String msg)
    {
        pw.println(msg);
    }

    private boolean authentication(String msg, PrintWriter pw, Scanner scanner)
    {
        String[] parts = msg.split("#");
        if (parts.length == 1)
        {
            pw.println("CLOSE#1");
            return false;
        }
        else if (parts.length == 2)
        {
            String command = parts[0];
            String user = parts[1];
            if (user.equals("Granno") && !chatServer.connectedNames.contains("Granno") || user.equals("Reder") && !chatServer.connectedNames.contains("Reder") || user.equals("Hansen") && !chatServer.connectedNames.contains("Hansen") || user.equals("Jensen") && !chatServer.connectedNames.contains("Jensen"))
            {
                myID = user;
                chatServer.connectedNames.add(myID);
                chatServer.addToSendQueue(chatServer.sendOnline());
            }
            else
            {
                pw.println("CLOSE#2");
                return false;
            }

        }
        return true;
    }

    private boolean handleCommand(String msg, PrintWriter pw, Scanner scanner)
    {
        String[] parts = msg.split("#");
        if (parts.length == 1)
        {
            if (parts[0].equals("CLOSE#"))
            {
                pw.println("CLOSE#0");
                chatServer.connectedNames.remove(myID);
                chatServer.addToSendQueue(chatServer.sendOnline());
                return false;
            }
            else
            {
                pw.println("CLOSE#1");
                return false;
            }
        }
        else if (parts.length == 3)
        {
            String token = parts[0];
            String argument = parts[1];
            String content = parts[2];
            switch (token)
            {
                case "SEND":
                    if (argument.equals("*"))
                    {
                        chatServer.addToSendQueue("MESSAGE#*#" + content);
                    }
                    else
                    {

                    }
                    break;

                default:
                    pw.println("CLOSE#1");
                    return false;
            }
        }
        return true;
    }

    private void handleClient() throws IOException
    {

        pw = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(socket.getInputStream());
        pw.println("Please connect before continuing - example: CONNECT#John");


        try
        {
            String message = "";
            String authenticate = "";
            boolean keepRunning = true;
            authenticate = scanner.nextLine();
            keepRunning = authentication(authenticate, pw, scanner);
            while (keepRunning)
            {
                message = scanner.nextLine(); //Blocking call
                keepRunning = handleCommand(message, pw, scanner);

            }
        } catch (Exception e)
        {
            System.out.println("");
            e.printStackTrace();
        }
        socket.close();
    }


    @Override
    public void run()
    {
        try
        {
            handleClient();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
