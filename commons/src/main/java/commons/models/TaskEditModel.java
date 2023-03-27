package commons.models;

import commons.List;

public class TaskEditModel {
    private String title;
    private String description;
    private List list;

    public TaskEditModel(String title, String description, List list){
        this.title = title;
        this.description = description;
        this.list=list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString(){
        return "TaskEditModel{" +
            "title='" + this.title + '\'' +
            ", description='" + this.description + '\'' +
            '}';
    }
}
