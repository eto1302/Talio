package commons.sync;

import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;

public class SubtaskEdited extends BoardUpdate{

    private int listID;
    private int taskID;
    private int subtaskID;
    private SubtaskEditModel subtaskEditModel;

    public SubtaskEdited(int boardID, int listID, int taskID,
                         int subtaskID, SubtaskEditModel subtaskEditModel){
        super(boardID);
        this.taskID = taskID;
        this.listID = listID;
        this.subtaskID = subtaskID;
        this.subtaskEditModel = subtaskEditModel;
    }

    public SubtaskEdited() {}

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getSubtaskID() {
        return subtaskID;
    }

    public void setSubtaskID(int subtaskID) {
        this.subtaskID = subtaskID;
    }

    public SubtaskEditModel getSubtaskEditModel() {
        return subtaskEditModel;
    }

    public void setSubtaskEditModel(SubtaskEditModel subtaskEditModel) {
        this.subtaskEditModel = subtaskEditModel;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.editSubtask(subtaskID, subtaskEditModel);
        return id;
    }

    @Override
    public void apply(IUserData data) {
        commons.List list = data.getCurrentBoard().getLists().stream()
                .filter(e -> e.getId() == listID).findFirst().orElse(null);
        commons.Task task = list.getTasks().stream().filter(e ->
                e.getId() == taskID).findFirst().orElse(null);
        commons.Subtask subtask = task.getSubtasks().stream().filter(e ->
                e.getId() == subtaskID).findFirst().orElse(null);
        subtask.setDescription(subtaskEditModel.getDescription());
        subtask.setIndex(subtaskEditModel.getIndex());
        subtask.setChecked(subtaskEditModel.isChecked());
        data.getShowCtrl().refreshSubtasks();
    }
}
