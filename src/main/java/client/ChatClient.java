package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient
{

    Socket socket;
    PrintWriter pw;

    public void connect(String address, int port) throws IOException
    {
        socket = new Socket(address, port);
        pw = new PrintWriter(socket.getOutputStream(), true);
        ServerReader sr = new ServerReader(socket.getInputStream());
        Thread t = new Thread(sr);
        t.start();


        Scanner keyboard = new Scanner(System.in);
        boolean keepRunning = true;
        while (keepRunning)
        {
            String msgToSend = keyboard.nextLine();
            pw.println(msgToSend);

            if (msgToSend.equals("CLOSE#"))
            {
                keepRunning = false;
            }
        }
        socket.close();


    }

    public static void main(String[] args) throws IOException
    {
        int DEFAULT_port = 8080;
        String DEFAULT_SERVER_IP = "localhost";
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
        new ChatClient().connect(ip, port);
    }


}
