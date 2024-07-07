package me.demo.spring_data_jpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("Member1",10,teamA);
        Member member2 = new Member("Member2",10,teamA);
        Member member3 = new Member("Member3",10,teamB);
        Member member4 = new Member("Member4",10,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.flush(); // 쿼리 즉시전송

        em.clear(); // 캐시 삭제

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();


        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.Team= " + member.getTeam());
        }


    }

}