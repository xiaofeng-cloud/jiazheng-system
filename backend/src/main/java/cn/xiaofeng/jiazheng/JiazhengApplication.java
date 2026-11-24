package cn.xiaofeng.jiazheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 家政服务工单系统后端启动类。
 * Spring Boot 会从这里开始加载配置、控制器和业务组件。
 */
@SpringBootApplication
public class JiazhengApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiazhengApplication.class, args);
    }
}
