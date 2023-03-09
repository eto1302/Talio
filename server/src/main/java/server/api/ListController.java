package server.api;

import org.springframework.web.bind.annotation.*;
import server.Services.ListService;

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
}
