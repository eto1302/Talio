package commons.models;

import java.util.Objects;

public class SubtaskEditModel {

    private String description;
    private boolean checked;

    public SubtaskEditModel(String description, boolean checked) {
        this.description = description;
        this.checked = checked;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtaskEditModel that = (SubtaskEditModel) o;
        return isChecked() == that.isChecked() &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), isChecked());
    }

    @Override
    public String toString() {
        return "SubtaskEditModel{" +
                "description='" + description + '\'' +
                ", checked=" + checked +
                '}';
    }
}
