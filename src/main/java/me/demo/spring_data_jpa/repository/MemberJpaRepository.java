package me.demo.spring_data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.demo.spring_data_jpa.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){ // 전체 조회 <- JPQL 활용
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id){ // 옵셔널 find 메서드
        Member member = em.find(Member.class,id);
        return Optional.ofNullable(member);
    }

    public long count(){ // 멤버 수 조회
        return em.createQuery("select count(m) from Member m",Long.class).getSingleResult();
    }

    public Member find(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findByPage(int age,int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc ", Member.class)
                .setParameter("age",age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age",age)
                .getSingleResult();
    }
}
