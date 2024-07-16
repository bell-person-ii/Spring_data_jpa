package me.demo.spring_data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import me.demo.spring_data_jpa.dto.MemberDto;
import me.demo.spring_data_jpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {

     List<Member>findByUsername(String username);
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

     @Modifying(clearAutomatically = true) // 벌크 연산이후 1차캐시 클리어를 자동으로 해줌
     @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
     int bulkAgePlus(@Param("age")int age);

     @Query("select m from Member m left join fetch m.team") // 패치 조인
     List<Member>findMemberFetchJoin();

     @Override
     @EntityGraph(attributePaths = {"team"}) // 패치 조인을 제공 받게 된다. findMemberFetchJoin() 메서드와 동일한 기능
     List<Member> findAll();

     @EntityGraph(attributePaths = {"team"}) // 패치 조인을 제공 받게 된다. findMemberFetchJoin() 메서드와 동일한 기능
     @Query("select m from Member m")
     List<Member> findMemberEntityGraph();

     @EntityGraph(attributePaths = ("team") ) // 간단할땐 이걸쓰고 복잡하면 JPQL로 패치 조인을 하자 - 김영한
     List<Member>findEntityGraphByUsername(@Param("username") String username);

     @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value = "true")) // 1차 캐시를 안쓰고 조회용으로만 사용해서 성능을 높일때 사용 <- 자주 사용할 일은 없을것으로 예상, 대부분의 문제는 쿼리 문제 -김영한-
     Member findReadOnlyByUsername(String username);


     @Lock(LockModeType.PESSIMISTIC_WRITE)
     List<Member>findLockByUsername(String username);
}
