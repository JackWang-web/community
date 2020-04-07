package life.majiang.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableScheduling
@EnableRedisHttpSession
@MapperScan(basePackages="life.majiang.community.mapper")
public class CommunityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CommunityApplication.class);
    }


}
