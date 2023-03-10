package server.api;

import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTagRepository implements TagRepository {


    public List<Tag> getTags() {
        return tags;
    }

    public List<String> getCalledMethods() {
        return calledMethods;
    }

    private final List<Tag> tags = new ArrayList<>();
    private final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Tag> findAll() {
        call("findAll");
        return this.tags;
    }

    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tag> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        call("count");
        return this.tags.size();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Tag entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Tag> S save(S entity) {
        call("save");
        this.tags.add(entity);
        return entity;
    }

    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Tag> findById(Integer integer) {
        return this.tags.stream().filter(x -> x.getId() == integer).findFirst();
    }

    @Override
    public boolean existsById(Integer integer) {
        return !this.tags.stream().filter(x -> x.getId() == integer).findAny().isEmpty();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Integer integer) {
        return null;
    }

    @Override
    public Tag getById(Integer integer) {
        var tag = this.tags.stream().filter(x -> x.getId() == integer).findFirst();
        return tag.isEmpty() ? null : tag.get();
    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Tag, R> R findBy(
            Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
