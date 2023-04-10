## 스프링 데이터 JPA

### 스프링 데이터 JPA 가 구현 클래스 대신 생성
- 스프링 데이터 JPA 가 프록시 구현 클래스를 생성하여 주입해준다
- `@Repository` 없어도 컴포넌트 스캔의 대상이 된다

### 공통 인터페이스 분석
- 스프링 데이터 (공통 기능 정의)
- 스프링 데이터 JPA (JPA 특화 기능 정의)

### 쿼리 메서드
- 메서드 이름으로 쿼리를 생성하는 방법 제공

### 스프링 데이터 JPA 페이징과 정렬
**페이징과 정렬 파라미터**
- `org.springframework.data.domain.Sort`: 정렬 기능
- `org.springframework.data.domain.Pageable`: 페이징 기능 (내부에 `Sort` 포함)

**특별한 반환 타입**
- `org.springframework.data.domain.Page`: 추가 count 쿼리 결과를 포함하는 페이징
- `org.springframework.data.domain.Slice`: 추가 count 쿼리 없이 다음 페이지만 확인 가능 (내부적으로 limit + 1 조회)
- `List` (자바 컬렉션) : 추가 count 쿼리 없이 결과만 반환

### 벌크 수정 쿼리
- 벌크 업데이트는 영속성 컨텍스트를 거치지 않고, 바로 DB 에 수정 사항을 반영한다
- 따라서 영속성 컨텍스트에는 변경 사항이 적용되지 않았으므로, 벌크 수정 이후에는 영속성 컨텍스트를 수정해줘야 한다 !
- 스프링 데이터 JPA 에서는 `clearAutomatically = true` 를 설정하면 된다
  - 쿼리 이후에 자동으로 영속성 컨텍스트 초기화 수행 