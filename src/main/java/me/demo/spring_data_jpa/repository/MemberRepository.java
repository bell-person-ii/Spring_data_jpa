package me.demo.spring_data_jpa.repository;

import me.demo.spring_data_jpa.dto.MemberDto;
import me.demo.spring_data_jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

     List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
     @Query("select m from Member m where m.username = :username and m.age = :age")
     List<Member> findUser(@Param("username")String username, @Param("age") int age);

     @Query("select m.username from Member m") //필드 조회
     List<String> findUsernameList();

     @Query("select new me.demo.spring_data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t") // dto 조회
     List<MemberDto> findMemberDto();
}
