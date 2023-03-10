package client.messageClients;

/**
 * Runnable that is called when a Message is succesfully received
 * Currently useless but can be extended with more functionality later
 */
public class Receipt implements Runnable{
    @Override
    public void run() {
        System.out.println("yay");
    }
}
