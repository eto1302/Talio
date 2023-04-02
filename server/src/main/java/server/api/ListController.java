package server.api;

import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.ListService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<commons.List> getList(@PathVariable int id){
        try {
            commons.List list = listService.getListById(id);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getByBoard/{id}")
    public ResponseEntity<java.util.List<commons.List>> getByBoard(@PathVariable int id) {
        try {
            java.util.List<commons.List> lists = listService.getAllListByBoard(id);
            return ResponseEntity.ok().body(lists);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findAll")
    @ResponseBody
    public List<commons.List> getAllLists(){
        return this.listService.getAllLists();
    }

    @PostMapping("/add/{boardId}")
    public IdResponseModel addList(@PathVariable int boardId, @RequestBody commons.List list) {
        return listService.addList(list, boardId);
    }

    @PostMapping("/edit/{boardId}/{listId}")
    public IdResponseModel editList(@PathVariable int boardId, @PathVariable int listId,
                                    @RequestBody ListEditModel model) {
        return listService.editList(boardId, listId, model);
    }

    @GetMapping("/delete/{boardId}/{listId}")
    public IdResponseModel removeList(@PathVariable int boardId, @PathVariable int listId) {
        return listService.removeList(listId, boardId);
    }
}
