package commons.messaging.Messages;

public class SuccessMessage implements Message{

    private boolean succ;

    public SuccessMessage() {
    }

    public SuccessMessage(boolean succ) {
        this.succ = succ;
    }

    public boolean getSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    @Override
    public void consume() {
        if(succ) System.out.println("Message Success");
        else System.out.println("Message Failure");
    }
}
