package commons.messaging.Messages;

public class TestMessage implements Message{

    private String info;

    public TestMessage() {}

    public TestMessage(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public void consume(){
        System.out.println(info);
    }
}
