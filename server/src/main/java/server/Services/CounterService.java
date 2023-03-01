package server.Services;


public class CounterService {
    private int counter;
    public void increase(){
        counter++;
    }
    public int value(){
        return this.counter;
    }
}
