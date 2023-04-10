package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
        assertThat(findMember).isEqualTo(saveMember);
    }

    /**
     * Spring Data JPA 트랜잭션 X 테스트
     */
    @Test
    public void transaction() {
        // @Transaction 이 없으면 오류가 발생하진 않으나, 서로 다른 트랜잭션에서 수행된다
        Member saveMember = memberRepository.save(new Member("234"));
        Member findMember = memberRepository.findById(saveMember.getId()).get();
        assertThat(saveMember).isNotEqualTo(findMember); // true
    }

    @Test
    public void basicCRUD() {
        Member member1  = new Member("member1");
        Member member2 = new Member("member");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void findMemberUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("AA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AA");
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AA", "BB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // dto
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // DB 를 바로 업데이트하므로, 영속성 컨텍스트에는 업데이트가 반영되지 않음 !
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        // 따라서 bulk 업데이트 이후 영속성 컨텍스트 초기화 !
        em.flush(); // 아직 반영되지 않은 쿼리 실행
        em.clear(); // 영속성 컨텍스트 초기화

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    // 지연 로딩 연관관계 조회
    @Test
    public void findMemberLazy() {
        // given
        // member1 -> teamA
        // member2 -> teamB

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

        // when
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // 쿼리 힌트 : READONLY 로 조회 - 스냅샷 생성 X
        Member findMember = memberRepository.findBeanOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        // 변경 감지 X
        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void projections() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member", 10, teamA);
        Member member2 = new Member("member", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<UsernameOnly> result = memberRepository.findProjectionByUsername("member");
        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly);
        }
    }
}