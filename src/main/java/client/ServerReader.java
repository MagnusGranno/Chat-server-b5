package client;

import java.io.InputStream;
import java.util.Scanner;

public class ServerReader implements Runnable
{
    Scanner scanner;
    Boolean bSr = true;
    String message = "";
    public ServerReader(InputStream is)
    {
        scanner = new Scanner(is);
    }

    @Override
    public void run()
    {

            while (bSr && scanner.hasNext())
            {

                message = scanner.nextLine();
                if (message.equals("CLOSE#0") || message.equals("CLOSE#1") || message.equals("CLOSE#2"))
                {
                    System.out.println(message);
                    bSr = false;
                }
                System.out.println(message);
            }
    }
}
