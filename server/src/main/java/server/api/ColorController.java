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

    @PostMapping("/add/{boardId}")
    public IdResponseModel create(@RequestBody Color color, @PathVariable int boardId) {
        return colorService.saveColor(color, boardId);
    }

    @GetMapping("/findAll")
    public List<Color> getAllColors() {
        return colorService.getAllColors();
    }

    @GetMapping("/delete/{id}")
    public IdResponseModel deleteColor(@PathVariable int id) {
        return colorService.deleteColor(id);
    }

    @PostMapping("/edit/{colorId}")
    public IdResponseModel editColor(@PathVariable int colorId, @RequestBody ColorEditModel model) {
        return colorService.editColor(colorId, model);
    }
}
