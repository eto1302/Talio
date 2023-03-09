package client.messageClients;

public class NoReceipt implements Runnable{
    @Override
    public void run() {
        throw new RuntimeException("not received");
    }
}
