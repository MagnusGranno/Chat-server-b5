package client;

import server.ChatServer;

import java.io.InputStream;
import java.util.Scanner;

//Jensen
public class ServerReader implements Runnable
{
    Scanner scanner;
    Boolean bSr = true;
    String message = "";
    ChatClient chatClient;
    public ServerReader(InputStream is, ChatClient chatClient)
    {
        scanner = new Scanner(is);
        this.chatClient = chatClient;
    }

    @Override
    public void run()
    {

            while (bSr && scanner.hasNext())
            {
                message = scanner.nextLine();

                if (message.equals("CLOSE#0") || message.equals("CLOSE#1") || message.equals("CLOSE#2"))
                {
                    bSr = false;
                    chatClient.setKeepRunning(false);
                    System.out.println(message);
                    System.out.println("Please hit return to stop client");

                }
                else
                {
                    System.out.println(message);
                }
            }
    }
}
