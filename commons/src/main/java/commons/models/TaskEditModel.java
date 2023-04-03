package commons.models;

import commons.List;

public class TaskEditModel {
    private String title;
    private String description;
    private List list;
    private int index;

    private int colorId;

    public TaskEditModel(String title, String description, int index, List list, int colorId){
        this.title = title;
        this.description = description;
        this.list=list;
        this.index=index;
        this.colorId = colorId;
    }

    public TaskEditModel() {}

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @Override
    public String toString(){
        return "TaskEditModel{" +
                "title='" + this.title + '\'' +
                ", description='" + this.description + '\'' +
                '}';
    }
}
