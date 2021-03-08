package client;

import java.io.InputStream;
import java.util.Scanner;

public class ServerReader implements Runnable {

    Scanner scanner;
    public ServerReader(InputStream is){
        scanner = new Scanner(is);
    }

    @Override
    public void run() {
        while(true)
        {
            String message = scanner.nextLine();
            System.out.println(message);
        }

    }
}
