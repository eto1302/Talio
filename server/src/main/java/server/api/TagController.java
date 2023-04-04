package server.api;

import commons.Tag;
import commons.models.IdResponseModel;
import commons.models.TagEditModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.TagService;

import java.util.Random;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;
    private Random random;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Tag> getTag(@PathVariable int id){
        try{
            Tag tag = tagService.getTagById(id);
            return ResponseEntity.ok(tag);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getByTask/{id}")
    public ResponseEntity<java.util.List<Tag>> getByTask(@PathVariable int id){
        try{
            java.util.List<Tag> tags = tagService.getAllTagsByTask(id);
            return ResponseEntity.ok(tags);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getByBoard/{id}")
    public ResponseEntity<java.util.List<Tag>> getByBoard(@PathVariable int id){
        try{
            java.util.List<Tag> tags = tagService.getAllTagsByBoard(id);
            return ResponseEntity.ok(tags);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findAll")
    @ResponseBody public java.util.List<Tag> findAll() {
        return this.tagService.findAll();
    }

    @PostMapping("/addToTask/{id}")
    public IdResponseModel addTagToTask(@PathVariable int id, @RequestBody Tag tag){
        return tagService.addTagToTask(tag, id);
    }

    @PostMapping("/addToBoard/{id}")
    public IdResponseModel addTagToBoard(@PathVariable int id, @RequestBody Tag tag){
        return tagService.addTagToBoard(tag, id);
    }

    @PutMapping("/edit/{id}")
    public IdResponseModel editTag(@PathVariable int id,
                                   @RequestBody TagEditModel model){
        return tagService.editTag(id, model);
    }

    @DeleteMapping("/remove/{id}")
    public IdResponseModel removeTag(@PathVariable int id){
        return tagService.removeTag(id);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = { "", "/" })
    public java.util.List<Tag> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> getTagById(@PathVariable int id) {
        Tag tag = tagService.getTagById(id);
        if (id < 0 || (tag == null)) {
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
    @GetMapping("rnd")
    public ResponseEntity<Tag> getRandom() {
        var tags = tagService.findAll();
        long n = tagService.count();
        var idx = random.nextInt((int) tagService.count());
        return ResponseEntity.ok(tags.get(idx));
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
