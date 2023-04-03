package commons.sync;

import commons.Tag;
import commons.Task;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class TagRemovedFromTask extends BoardUpdate{

    private Tag tag;
    private Task task;

    public TagRemovedFromTask(int boardID, Tag tag, Task task) {
        super(boardID);
        this.tag = tag;
        this.task = task;
    }

    public TagRemovedFromTask() {
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.removeTagFromTask(tag.getId(), task.getId());
        return id;
    }

    @Override
    public void apply(IUserData data) {
        data.getShowCtrl().removeTagFromTask(tag, task);
    }
}
