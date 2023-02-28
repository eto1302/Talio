package server;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

public class CounterService {
    private int counter;
    public void increase(){
        counter++;
    }
    public int value(){
        return this.counter;
    }
}
