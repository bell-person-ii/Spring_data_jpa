package me.demo.spring_data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.demo.spring_data_jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJpqRepository{
    @PersistenceContext
    private EntityManager em;
    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public Member find(Long id){
        return em.find(Member.class,id);
    }
}
