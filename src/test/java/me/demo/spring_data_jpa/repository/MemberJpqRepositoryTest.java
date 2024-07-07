package me.demo.spring_data_jpa.repository;

import me.demo.spring_data_jpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpqRepositoryTest {

    @Autowired MemberJpqRepository memberJpqRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberJpqRepository.save(member);
        Member findMember = memberJpqRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId()); // core.api.Assertions
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 영속성 컨텍스트 동일성 보장 => 같은 인스턴스 보장 (1차 캐시)
    }
}