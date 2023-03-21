package commons.messaging.Messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import client.sync.*;

/**
 * Message Interface
 * All Messages sent through the messaging system implement this interface.
 * void consume() -> method used to consume the message,
 * this will be run on all clients receiving the message.
 *
 * How to make your own Message:
 * 1. implement Message in yourMessage and override consume()
 *      you can add any other fields and methods
 *      remeber to put it in commons, preferably in this package
 * 2. Add yourMessage to @JsonSubTypes
 *      @JsonSubTypes.Type(value = yourMessage.class,
 *      name = "your fun and unique name")
 *      if you don't do this, the message converter
 *      won't be able to deserialize the json message to your class
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = TestMessage.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TestMessage.class, name = "test"),
        @JsonSubTypes.Type(value = SuccessMessage.class, name = "success"),
        @JsonSubTypes.Type(value = BoardUpdate.class, name = "boardUpdate"),
        @JsonSubTypes.Type(value = ListAdded.class, name = "listAdded"),
        @JsonSubTypes.Type(value = ListDeleted.class, name = "listDeleted"),
        @JsonSubTypes.Type(value = ListEdited.class, name = "listEdited")
})
public interface Message {
    void consume();
}
