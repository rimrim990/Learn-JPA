package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// jpa 데이터 변경은 트랜잭션 안에서 실행되어야 함 - public 메서드
@Transactional(readOnly = true)
// final 이 있는 필드만 생성자에 포함시킨다
@RequiredArgsConstructor
public class MemberService {

    // 필드 주입은 스프링 주입 없이는 값의 변경이 불가능
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검축
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // UNIQUE 제약조건 걸기
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public Long update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        // 변경 감지
        member.setName(name);
        // 커맨드와 커리 분리
        return member.getId();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
