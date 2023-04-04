package commons.models;

import java.util.Objects;

public class SubtaskEditModel {

    private String description;
    private boolean checked;
    private int index;

    public SubtaskEditModel(String description, boolean checked, int index) {
        this.description = description;
        this.checked = checked;
        this.index=index;
    }

    public SubtaskEditModel() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtaskEditModel that = (SubtaskEditModel) o;
        return isChecked() == that.isChecked() &&
                getIndex() == that.getIndex() &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), isChecked(), getIndex());
    }

    @Override
    public String toString() {
        return "SubtaskEditModel{" +
                "description='" + description + '\'' +
                ", checked=" + checked +
                ", index=" + index+
                '}';
    }
}
