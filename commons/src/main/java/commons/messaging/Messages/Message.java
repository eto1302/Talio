package commons.messaging.Messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = TestMessage.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TestMessage.class, name = "test"),
        @JsonSubTypes.Type(value = SuccessMessage.class, name = "success")
})
public interface Message {
    void consume();
}
