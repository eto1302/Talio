package commons.sync;

import commons.Task;
import commons.Tag;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.ArrayList;
import java.util.List;

public class TagAddedToTask extends BoardUpdate{
    private Task task;
    private Tag tag;

    public TagAddedToTask(int boardID, Task task, Tag tag) {
        super(boardID);
        this.task = task;
        this.tag = tag;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public TagAddedToTask() {
        super();
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addTagToTask(tag, task.getId());
        return id;
    }

    @Override
    public void apply(IUserData data) {
        if(task.getTags() == null) {task.setTags(new ArrayList<>());}
        List<Tag> fresh = task.getTags();
        fresh.add(tag);
        task.setTags(fresh);
        if(tag.getTasks() == null) {tag.setTasks(new ArrayList<>());}
        tag.getTasks().add(task);
        tag.getTaskIDs().add(task.getId());
        data.getShowCtrl().addTagToTask(tag, task);
    }
}
