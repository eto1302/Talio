package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.TagService;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;
    private final Random random;

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public TagController(TagService tagService, Random rnd) {
        this.tagService = tagService;
        this.random = rnd;
    }

    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> getTagById(@PathVariable int id) {
        var sth = tagService.findAll();
        if (id < 0 || (tagService.getTagById(id) == null)) {
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

    @PostMapping("/addToBoard/{id}")
    public IdResponseModel addTagToBoard(@PathVariable int id, @RequestBody Tag tag){
        return tagService.addTagToBoard(tag, id);
    }

    @PostMapping("/edit/{id}")
    public IdResponseModel editTag(@PathVariable int id,
                                   @RequestBody TagEditModel model){
        return tagService.editTag(id, model);
    }

    @GetMapping("/remove/{id}")
    public IdResponseModel removeTag(@PathVariable int id){
        return tagService.removeTag(id);
    }

//    private static boolean isNullOrEmpty(String s) {
//        return s == null || s.isEmpty();
//    }
//
//    public TagController(TagService tagService, Random rnd) {
//        this.tagService = tagService;
//        this.random = rnd;
//    }
//
//    @GetMapping(path = { "", "/" })
//    public List<Tag> getAll() {
//        return tagService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    @ResponseBody
//    public ResponseEntity<Object> getTagById(@PathVariable int id) {
//        var sth = tagService.findAll();
//        if (id < 0 || (tagService.getTagById(id) == null)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(tagService.getTagById(id));
//    }
//    @PostMapping(path = { "", "/" })
//    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
//
//        if (isNullOrEmpty(tag.getName())) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Tag saved = tagService.save(tag);
//        return ResponseEntity.ok(saved);
//    }
//    @GetMapping("rnd")
//    public ResponseEntity<Tag> getRandom() {
//        var tags = tagService.findAll();
//        long n = tagService.count();
//        var idx = random.nextInt((int) tagService.count());
//        return ResponseEntity.ok(tags.get(idx));
//    }
}
