package commons.sync;

import commons.Tag;
import commons.Task;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

import java.util.List;


public class TagDeleted extends BoardUpdate{

    private Tag tag;
    private int taskID;

    public TagDeleted(int boardID, Tag tag) {
        super(boardID);
        this.tag = tag;
        this.taskID = taskID;
    }

    public TagDeleted() {super();}

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        return server.removeTagFromBoard(tag.getId(), tag.getBoardId());
        //TODO: Find the correct taskID (maybe in a different method)
    }

    @Override
    public void apply(IUserData data) {
        //TODO: find a way to refresh the tasks
        data.refresh();
        data.getShowCtrl().deleteTag(tag);
    }


    private void cleanTasks(List<Task> tasks){
        if(tasks == null || tasks.isEmpty()) return;
        for(Task t: tasks){
            t.getTags().remove(tag);
        }
    }
}
