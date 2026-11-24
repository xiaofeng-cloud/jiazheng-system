package cn.xiaofeng.jiazheng;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired PasswordEncoder encoder;

    @BeforeEach
    void prepareUserTable() {
        jdbc.execute("CREATE TABLE IF NOT EXISTS sys_user (id BIGINT, username VARCHAR(50), password VARCHAR(100), real_name VARCHAR(50), phone VARCHAR(20), user_type INT, status INT, PRIMARY KEY(id))");
        jdbc.update("DELETE FROM sys_user");
        jdbc.update("INSERT INTO sys_user(id, username, password, real_name, phone, user_type, status) VALUES (1, 'admin', ?, '管理员', '13800000001', 1, 1)", encoder.encode("123456"));
    }

    @Test
    void loginReturnsUnifiedJwtResponse() throws Exception {
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200)).andExpect(jsonPath("$.data.token").isNotEmpty()).andExpect(jsonPath("$.data.roleCode").value("ADMIN"));
    }
}
