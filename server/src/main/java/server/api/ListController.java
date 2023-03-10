package server.api;

import org.springframework.web.bind.annotation.*;
import server.Services.ListService;

import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getList(@PathVariable int id){
        return this.listService.getListById(id).toString();
    }

    @GetMapping("/findAll")
    @ResponseBody
    public List<commons.List> getAllLists(){
        return this.listService.getAllLists();
    }

    @PostMapping("/add/{boardId}")
    public int addList(@PathVariable int boardId, @RequestBody commons.List list) {
        return listService.addList(list, boardId);
    }

    @GetMapping("/rename/{listId}/{name}")
    public boolean renameList(@PathVariable int listId, @PathVariable String name) {
        return listService.renameList(listId, name);
    }

    @GetMapping("/delete/{boardId}/{listId}")
    public boolean removeList(@PathVariable int boardId, @PathVariable int listId) {
        return listService.removeList(listId, boardId);
    }
}
