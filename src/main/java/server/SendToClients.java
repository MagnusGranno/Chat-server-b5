package server;

import java.util.concurrent.BlockingQueue;

class SendToClients implements Runnable {
    ChatServer chatServer;
    BlockingQueue<String> sendQueue;

    public SendToClients(ChatServer chatServer, BlockingQueue<String> sendQueue)
    {
        this.chatServer = chatServer;
        this.sendQueue = sendQueue;
    }


    @Override
    public void run() {
        while(true)
        {

            try {
                String message = sendQueue.take();
                chatServer.sendToAll(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
