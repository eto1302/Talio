package server.Services;

import commons.List;
import org.springframework.stereotype.Service;
import commons.Task;
import server.database.ListRepositoy;

import java.util.ArrayList;


@Service
public class ListService {

    ListRepositoy listRepositoy;
    public ListService(ListRepositoy listRepositoy){
        this.listRepositoy = listRepositoy;
    }
    public List getList(){
        java.util.List<Task> tasks = new ArrayList<>();
        return  List.create("Urgent!", tasks);
    }

    public List getListById(int id){
        return this.listRepositoy.getById(id);
    }

}
