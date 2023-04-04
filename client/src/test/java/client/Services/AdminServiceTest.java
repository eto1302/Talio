package client.Services;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AdminServiceTest {
    @MockBean
    private ServerUtils mockServerUtils = mock(ServerUtils.class);
    private AdminService adminService;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        adminService = new AdminService(mockServerUtils);
    }

    @Test
    public void verifyAdminTest(){
        when(mockServerUtils.verifyAdmin(any(String.class))).thenReturn(true);
        boolean response = adminService.verifyAdmin("test");
        assertTrue(response);
        verify(mockServerUtils,times(1)).verifyAdmin(any(String.class));
    }
}
