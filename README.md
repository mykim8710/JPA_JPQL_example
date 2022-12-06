## Java ORM JPA - JPQL
- JPQL : Java Persistence Query Language
  - JPQL은 객체지향 쿼리 언어다.
  - 테이블을 대상으로 쿼리하는 것이 아니라 **엔티티 객체를 대상으로 쿼리**한다.
  - JPQL은 SQL을 추상화해서 **특정 데이터베이스 SQL에 의존하지 않는다.**
  - JPQL은 결국 SQL로 변환된다.

- JPQL 기본문법
```
select_문 :: = 
  select_절
    from_절 
    [where_절] 
    [groupby_절] 
    [having_절] 
    [orderby_절]

update_문 :: = update_절 [where_절]
delete_문 :: = delete_절 [where_절]
```
  - 엔티티와 속성은 대소문자 구분 O (Member, age)
  - JPQL 키워드는 대소문자 구분 X (SELECT, FROM, where)
  - 엔티티 이름 사용, 테이블 이름이 아님(Member)
  - 별칭은 필수(**m**) (as는 생략가능)

- 집합과 정렬
```
select
	COUNT(m),    // 회원 수
	SUM(m.age),  // 나이 합
	AVG(m.age),  // 평균 나이 
	MAX(m.age),  // 최대 나이 
	MIN(m.age)   // 최소 나이
  from Member m
```
  - GROUP BY, HAVING
  - ORDER BY

- TypeQuery, Query
  - TypeQuery : 반환 타입이 명확할 때 사용
  - Query : 반환 타입이 명확하지 않을 때 사용

- 결과 조회 API
  - **query.getResultList() :** 결과가 하나 이상일 때, 리스트 반환
    - 결과가 없으면 빈 리스트 반환
  - **query.getSingleResult():** 결과가 **정확히** 하나, 단일 객체 반환
    - 결과가 없으면 **: `javax.persistence.NoResultException`**
    - 둘 이상이면 **: `javax.persistence.NonUniqueResultException`**

- 파라미터 바인딩 
  - 이름 기준
  - 위치 기준 : 사용 X

- 프로젝션(SELECT)
  - SELECT 절에 **조회할 대상을 지정**하는 것
  - 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입)
  - 프로젝션 - 여러 값 조회
    - 1. Query 타입으로 조회
    - 2. Object[] 타입으로 조회
    - 3. new 명령어로 조회
      - 단순 값을 DTO로 바로 조회
        - `SELECT new hello.jpa.jpql.dto.MemberDto(m.username, m.age) FROM Member m`
        - 패키지명을 포함한 전체 dto 클래스명 입력
        - 순서와 타입이 일치하는 생성자 필요