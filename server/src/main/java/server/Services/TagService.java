package server.Services;

import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;

@Service
public class TagService {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Tag getTagById(int id) {
        if(!tagRepository.existsById(id)) return null;
        return tagRepository.getById(id);
    }

    public List<Tag> findAll() { return tagRepository.findAll(); }

    public Tag save(Tag tag) { return tagRepository.save(tag); }

    public long count() {return tagRepository.count();}
}
