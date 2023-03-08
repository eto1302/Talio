package server.api;

import commons.Tag;
import org.springframework.web.bind.annotation.*;
import server.Services.TagService;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @GetMapping("/{id}")
    @ResponseBody
    public String getTagById(@PathVariable int id) {
        Tag tag = tagService.getTagById(id);
        return tag.toString();
    }
}
