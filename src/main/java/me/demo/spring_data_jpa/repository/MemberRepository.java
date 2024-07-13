package me.demo.spring_data_jpa.repository;

import me.demo.spring_data_jpa.dto.MemberDto;
import me.demo.spring_data_jpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

     List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
     @Query("select m from Member m where m.username = :username and m.age = :age")
     List<Member> findUser(@Param("username")String username, @Param("age") int age);

     @Query("select m.username from Member m") //필드 조회
     List<String> findUsernameList();

     @Query("select new me.demo.spring_data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t") // dto 조회
     List<MemberDto> findMemberDto();

     @Query("select m from Member m where m.username in :names")
     List<Member> findByNames(@Param("names") Collection<String> names);

     List<Member>findListByUsername(String username); // 컬렉션
     Member findMemberByUsername(String username); //  단건
     Optional<Member>findOptionalByUsername(String username); // 단건 optional

     Page<Member> findByAge(int age, Pageable pageable); // Page 와 Slice -> 번호 눌러서 움직일땐 Page, 모바일 페이지 처럼 더보기 기반은 Slice

     @Query(value = "select m from Member m left join m.team t", countQuery = "select count (m.username) from Member m") // count를 위한 쿼리에서 join이 일어나는 것은 비효율적이므로 count 쿼리를 분리하는 방법
     Page<Member>findByAge2(int age,Pageable pageable);
}
