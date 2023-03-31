package commons;

import com.fasterxml.jackson.annotation.JsonView;

// This interface is used to specify which fields of a Board object should be included in the JSON,
// as not all fields are needed to displayed in the admin scene. Only the fields with
// @JsonView(BoardSummary.class) annotation will be included in the JSON.
public interface BoardSummary extends JsonView {}
