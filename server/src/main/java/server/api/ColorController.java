package server.api;

import commons.Color;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.ColorService;

import java.util.List;

@RestController
@RequestMapping("/color")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public ResponseEntity<Color> getColorById(@PathVariable int id) {
        try {
            Color color = colorService.getColorById(id);
            return ResponseEntity.ok(color);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/add")
    public IdResponseModel create(@RequestBody Color color) {
        return colorService.saveColor(color);
    }

    @PostMapping("/add/{colorId}/{boardId}")
    public IdResponseModel setToBoard(@PathVariable int colorId, @PathVariable int boardId) {
        return colorService.setToBoard(colorId, boardId);
    }

    @GetMapping("/findAll")
    public List<Color> getAllColors() {
        return colorService.getAllColors();
    }

    @GetMapping("/delete/{boardId}/{colorId}")
    public IdResponseModel deleteColor(@PathVariable int boardId, @PathVariable int colorId) {
        return colorService.deleteColor(boardId, colorId);
    }

    @PostMapping("/edit/{colorId}")
    public IdResponseModel editColor(@PathVariable int colorId, @RequestBody ColorEditModel model) {
        return colorService.editColor(colorId, model);
    }
}
