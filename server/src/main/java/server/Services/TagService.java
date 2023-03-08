package server.Services;

import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

@Service
public class TagService {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Tag getTagById(int id) {
        return tagRepository.getById(id);
    }
}
