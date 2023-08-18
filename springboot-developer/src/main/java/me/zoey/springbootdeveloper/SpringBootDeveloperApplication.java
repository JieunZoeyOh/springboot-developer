package me.zoey.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 여기서 스프링 부트가 시작된다.
 */
@EnableJpaAuditing //created_at, updated_at 자동 업데이트
@SpringBootApplication //스프링 부트 사용에 필요한 기본 설정을 해준다.
public class SpringBootDeveloperApplication {

    public static void main(String[] args) {
        //SpringApplication.run() 메서드는 애플리케이션을 실행한다.
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }

}
