package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient
{
    Socket socket;
    PrintWriter pw;
    private boolean keepRunning;

    public void setKeepRunning(boolean keepRunning)
    {
        this.keepRunning = keepRunning;
    }

    public void connect(String address, int port) throws IOException
    {
        socket = new Socket(address, port);
        pw = new PrintWriter(socket.getOutputStream(), true);
        ServerReader sr = new ServerReader(socket.getInputStream(),this);
        Thread t = new Thread(sr);
        t.start();
        Scanner keyboard = new Scanner(System.in);
        keepRunning = true;
        while (keepRunning)
        {
            String msgToSend = keyboard.nextLine();
            pw.println(msgToSend);

        }

        socket.close();

    }

    public static void main(String[] args) throws IOException
    {
        int DEFAULT_port = 8080;
        String DEFAULT_SERVER_IP = "206.81.26.43";
        int port = DEFAULT_port;
        String ip = DEFAULT_SERVER_IP;
        if (args.length == 2)
        {
            try
            {
                ip = args[0];
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e)
            {
                System.out.println("Invalid port or ip, using defaults port :");
            }
        }
        ChatClient chatClient = new ChatClient();
        chatClient.connect(ip, port);


    }
}
