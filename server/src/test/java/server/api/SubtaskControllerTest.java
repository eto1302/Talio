package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import server.Services.SubtaskService;


@SpringBootTest
@AutoConfigureMockMvc
class SubtaskControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private SubtaskService mockService;

    private transient Subtask subtask1 = Subtask.create("Doing homework", false, 1);
    private transient Subtask subtask2 = Subtask.create("Random Subtask", true, 1);


}