package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 프로퍼티 이름
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
