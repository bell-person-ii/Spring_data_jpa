package me.demo.spring_data_jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.demo.spring_data_jpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{ // 구현 클래스명은 반드시 Impl로 맞춰줘야 한다 그래야 DATA JPA가 읽을수 있다.

    private final EntityManager em;
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
