package commons.messaging.Messages;

public class SuccessMessage implements Message{

    private boolean isSuccess;

    public SuccessMessage() {
    }

    public SuccessMessage(boolean success) {
        this.isSuccess = success;
    }

    public boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean succ) {
        this.isSuccess = succ;
    }

    @Override
    public void consume() {
        if(isSuccess) System.out.println("Message Success");
        else System.out.println("Message Failure");
    }
}
