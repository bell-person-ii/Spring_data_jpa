package me.demo.spring_data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.demo.spring_data_jpa.entity.Member;
import me.demo.spring_data_jpa.repository.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id")Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id")Member member){ // 위 메서드와 동일한 기능을 수행하도록 스프링 부트에서 컨버팅을 해줌 <- 가능은 하지만 권장하지는 않는 방식
        return member.getUsername();
    }

    @PostConstruct
    public void init(){
        memberRepository.save(new Member("userA"));
    }
}
