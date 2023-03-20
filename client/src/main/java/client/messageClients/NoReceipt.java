package client.messageClients;

/**
 * Runnable that is called when a Message is not successfully received
 * Currently fairly useless, but can be exteneded later.
 */
public class NoReceipt implements Runnable{
    @Override
    public void run() {
        throw new RuntimeException("The Message was not received!");
    }
}
