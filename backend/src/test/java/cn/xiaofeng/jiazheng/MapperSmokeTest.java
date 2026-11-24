package cn.xiaofeng.jiazheng;

import cn.xiaofeng.jiazheng.auth.SysUserMapper;
import cn.xiaofeng.jiazheng.order.WorkOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MapperSmokeTest {
    @Autowired JdbcTemplate jdbc;
    @Autowired SysUserMapper userMapper;
    @Autowired WorkOrderMapper orderMapper;

    @BeforeEach
    void prepareTables() {
        jdbc.execute("CREATE TABLE IF NOT EXISTS sys_user (id BIGINT, username VARCHAR(50), password VARCHAR(100), real_name VARCHAR(50), phone VARCHAR(20), user_type INT, status INT, PRIMARY KEY(id))");
        jdbc.execute("CREATE TABLE IF NOT EXISTS work_order (id BIGINT, order_no VARCHAR(40), customer_id BIGINT, provider_id BIGINT, service_item_id BIGINT, status VARCHAR(20), scheduled_date DATE, scheduled_time VARCHAR(30), updated_at TIMESTAMP, PRIMARY KEY(id))");
        jdbc.update("DELETE FROM sys_user"); jdbc.update("DELETE FROM work_order");
        jdbc.update("INSERT INTO sys_user(id, username, password, user_type, status) VALUES (1001, 'customer01', 'hash', 3, 1)");
        jdbc.update("INSERT INTO work_order VALUES (1, 'WO1', 1001, NULL, 1, '待分配', CURRENT_DATE, '09:00-12:00', CURRENT_TIMESTAMP)");
    }

    @Test
    void mapsDatabaseRowsToRecords() {
        assertThat(userMapper.findByUsername("customer01").roleCode()).isEqualTo("CUSTOMER");
        assertThat(orderMapper.findStatus(1L).status()).isEqualTo("待分配");
    }
}
