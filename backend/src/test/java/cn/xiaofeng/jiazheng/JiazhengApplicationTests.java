package cn.xiaofeng.jiazheng;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 项目最小启动测试。
 * 第一阶段先用它确认 Spring Boot 工程可以加载，后续模块测试会在此基础上增加。
 */
@SpringBootTest
@ActiveProfiles("test")
class JiazhengApplicationTests {

    @Test
    void contextLoads() {
        // 上下文能够启动即说明当前基础配置没有阻断应用加载。
    }
}
