package server.api;

import antlr.StringUtils;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.TagService;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> getTagById(@PathVariable int id) {
        if (id < 0 || (tagService.getTagById(id) != null)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tagService.getTagById(id));
    }
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {

        if (isNullOrEmpty(tag.getName())) {
            return ResponseEntity.badRequest().build();
        }

        Tag saved = tagService.save(tag);
        return ResponseEntity.ok(saved);
    }
}
