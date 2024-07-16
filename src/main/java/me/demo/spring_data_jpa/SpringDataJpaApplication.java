package me.demo.spring_data_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);}

    @Bean
    public AuditorAware<String> auditorProvider(){ // 실제 프로젝트라면 시큐리티 컨텍스트 홀더에서 유저 아이디를 뽑아와야함 <- 데이터를 생성하고, 수정한 작성자가 누구인지 DB에 저장할수 있음
        return ()-> Optional.of(UUID.randomUUID().toString());
    }
}
