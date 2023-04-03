package commons.sync;

import commons.Tag;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;
import commons.models.TagEditModel;

public class TagEdited extends BoardUpdate{
    private Tag tag;

    private TagEditModel model;

    public TagEdited(int boardID, Tag tag, TagEditModel model) {
        super(boardID);
        this.tag = tag;
        this.model = model;
    }

    public TagEdited(){}

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public TagEditModel getModel() {
        return model;
    }

    public void setModel(TagEditModel model) {
        this.model = model;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.editTag(tag.getId(), model);
        return id;
    }

    @Override
    public void apply(IUserData data) {
        tag.setName(model.getName());
        tag.setColor(model.getColor());
        data.getShowCtrl().editTag(tag);
    }
}
