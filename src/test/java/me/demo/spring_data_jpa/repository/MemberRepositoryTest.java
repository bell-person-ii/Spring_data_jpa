package me.demo.spring_data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.demo.spring_data_jpa.dto.MemberDto;
import me.demo.spring_data_jpa.entity.Member;
import me.demo.spring_data_jpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired
    EntityManager em;
    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get(); // 옵셔널 벗기기
        assertThat(findMember.getId()).isEqualTo(member.getId()); // core.api.Assertions
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 영속성 컨텍스트 동일성 보장 => 같은 인스턴스 보장 (1차 캐시)
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단일 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        long count = memberRepository.count();
        assertThat(count) . isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){

        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA",15);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void testQuery(){

        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameTest(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = "+s);
        }
    }

    @Test
    public void findMemberDto(){

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        m1.changeTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for(Member member : result){
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findListByUsername("AAA");
        Member member = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");

        System.out.println("members = " + members);
        System.out.println("member = " + member);
        System.out.println("optionalMember = " + optionalMember);


        List<Member> members2 = memberRepository.findListByUsername("CCC"); // 컬렉션 반환시 없는 데이터를 조회하는 경우 -> null 이 아님이 보장된다
        System.out.println("members2.size() = " + members2.size());

        Member member2 = memberRepository.findMemberByUsername("CCC"); // 단건 조회시 없는 데이터를 조회하는 경우 - > null이 반환된다.
        System.out.println("member2 = " + member2); // null 반환

        Optional<Member> optionalMember2 = memberRepository.findOptionalByUsername("CCC"); //옵셔널 반환형을 사용하는 경우 <- 단건 조회 null이어도 대처가 유연해진다.
        System.out.println("optionalMember2 = " + optionalMember2);


        /* 단건 조회시 복수 조회가 된 경우*/
        System.out.println("단건 조회시 복수 조회가 된 경우");
        Member m3 = new Member("AAA",10);
        memberRepository.save(m3);

        Assertions.assertThatThrownBy(()-> memberRepository.findOptionalByUsername("AAA")).isInstanceOf(IncorrectResultSizeDataAccessException.class); // IncorrectResultSizeDataAccessException 발생

    }

    @Test
    public void paging(){

        // given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge2(age, pageRequest); // total count도 동시에 처리됨

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

        //then

        List<Member>content = page.getContent();
        long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3); // 가져온 개수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 개수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 수
        assertThat(page.isFirst()).isTrue(); // 현재 페이지가 첫번째 페이지 인지
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 존재하지는지
    }

    @Test
    public void bulkUpdate(){ // 벌크 연산
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));

        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear(); // 벌크 연산 후에는 영속성 컨텍스트를 DB에서 다시 불러와서 초기화 해야한다.

        List<Member> result = memberRepository.findByUsername("member5");
        Member member = result.get(0);
        System.out.println("member = " + member);


        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 프록시 객체가 조회됨
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); // 패치 조인이 적용되기 이전에는 team 테이블에 select 문이 멤버 숫자만큼 날아가게 된다. <- N+1 문제
        }
    }
    @Test
    public void findMemberLazyFetchJoin(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberFetchJoin();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 실제 엔티티 객체가 조회됨
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); // team 과 member 테이블을 조인해서 한방에 가져옴 <- N+1 문제 해결
        }
    }

    @Test
    public void findEntityGraphByUsername(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 실제 엔티티 객체가 조회됨
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); // team 과 member 테이블을 조인해서 한방에 가져옴 <- N+1 문제 해결
        }
    }

    @Test
    public void queryHint(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findmember = memberRepository.findReadOnlyByUsername("member1");
        findmember.changeUsername("member2"); // 더티 체킹 무력화 상태

        em.flush(); // update 쿼리가 안날아감
    }

    @Test
    public void lock(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

    }
    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }
}