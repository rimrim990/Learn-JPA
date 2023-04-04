package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 컨테이너에서 테스트 실행
@Transactional // 테스트 rollback
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        // 영속성 컨텍스트의 내용을 강제로 DB 에 flush
        // em.flush()
        // 동일한 영속성 컨텍스트 안에 있으므로 동일한 객체 반환
        assertThat(member).isEqualTo(memberRepository.findOne(savedId));

        // 트랜잭션이 커밋되지 않고 롤백되므로 flush 가 발생하지 않음 - 영속성 컨텍스트의 변경 사항을 DB 에 반영하지 않음
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        // when
        memberService.join(member1);

        // then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }
}