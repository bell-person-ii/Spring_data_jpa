package me.demo.spring_data_jpa.repository;

import me.demo.spring_data_jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
