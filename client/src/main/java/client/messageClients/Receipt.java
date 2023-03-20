package client.messageClients;

/**
 * Runnable that is called when a Message is successfully received
 * Currently useless but can be extended with more functionality later
 */
public class Receipt implements Runnable{
    @Override
    public void run() {
        System.out.println("The Message has been received successfully!");
    }
}
