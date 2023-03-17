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

    @GetMapping("/edit/{listId}/{name}/{background}/{font}")
    public boolean editList(@PathVariable int listId, @PathVariable String name,
                              @PathVariable String background, @PathVariable String font) {
        return listService.editList(listId, name, background, font);
    }

    @GetMapping("/delete/{boardId}/{listId}")
    public boolean removeList(@PathVariable int boardId, @PathVariable int listId) {
        return listService.removeList(listId, boardId);
    }
}
