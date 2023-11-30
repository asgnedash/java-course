package org.example.fintech.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void noAuthorizedTest() throws Exception {
        mockMvc.perform(get("/weather/Moscow"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin_for_test", roles = "ADMIN")
    public void isAuthorizedByAdminRoleTest() throws Exception {
        mockMvc.perform(get("/weather/Moscow"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user_for_test", roles = "USER")
    public void isAuthorizedByUserRoleTest() throws Exception {
        mockMvc.perform(get("/weather/Moscow"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin_for_test", roles = "ADMIN")
    public void adminHasAccessTest() throws Exception {
        mockMvc.perform(delete("/api/weather/Sochi"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user_for_test", roles = "USER")
    public void userHasNoAccessTest() throws Exception {
        mockMvc.perform(delete("/api/weather/Sochi"))
                .andExpect(status().isForbidden());
    }
}
