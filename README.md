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

  - [집합과 정렬]
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

  - [TypeQuery, Query]
    - TypeQuery : 반환 타입이 명확할 때 사용
    - Query : 반환 타입이 명확하지 않을 때 사용

  - [결과 조회 API]
    - **query.getResultList() :** 결과가 하나 이상일 때, 리스트 반환
      - 결과가 없으면 빈 리스트 반환
    - **query.getSingleResult():** 결과가 **정확히** 하나, 단일 객체 반환
      - 결과가 없으면 **: `javax.persistence.NoResultException`**
      - 둘 이상이면 **: `javax.persistence.NonUniqueResultException`**

  - [파라미터 바인딩]
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

- 페이징
  - JPA는 페이징을 다음 두 API로 추상화
  - **setFirstResult**(int startPosition) : 조회 시작 위치(0부터 시작)
  - **setMaxResults**(int maxResult) : 조회 할 데이터 수
  - 즉, 몇번째부터 몇개 가져올래?

- 조인
  - 내부 조인 : inner join
    - SELECT m FROM Member m [INNER] JOIN m.team t
  - 외부 조인 : outer join
    - SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
  - 세타 조인 : seta join : 연관관계가 없을 경우
    - select count(m) from Member m, Team t where m.username= t.name
  - 조인 - ON 절을 활용한 조인(JPA 2,1부터 지원)
    - 조인 대상 필터링
      - SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'
    - 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)
      - SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name

- 서브쿼리
  - 서브 쿼리 지원 함수
    - [NOT] EXISTS (subquery): 서브쿼리에 결과가 존재하면 참
      - {ALL | ANY | SOME} (subquery)
      - ALL : 모두 만족하면 참
      - ANY, SOME: 같은 의미, 조건을 하나라도 만족하면 참
      - [NOT] IN (subquery): 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
  - JPA 서브 쿼리 한계
    - JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
    - SELECT 절도 가능(하이버네이트에서 지원)
    - **FROM 절의 서브쿼리는 현재 JPQL에서 불가능**
      - 조인으로 풀 수 있으면 풀어서 해결
      - 네이티브 쿼리 사용
      - 쿼리를 두번날린 뒤 애플리케이션에서 조립

- JPQL 타입표현과 기타식
  - **JPQL 타입 표현**
    - 문자: ‘HELLO’, ‘She’’s’
    - 숫자: 10L(Long), 10D(Double), 10F(Float)
    - Boolean: TRUE, FALSE
    - ENUM: hello.jpa.jpql.entity.MemberType.Admin (패키지명 포함)
    - 엔티티 타입: TYPE(m) = Member (상속 관계에서 사용)
  - **JPQL 기타**
    - SQL과 문법이 같은 식
    - EXISTS, IN
    - AND, OR, NOT
    - =, >, >=, <, <=, <>
    - BETWEEN, LIKE, **IS NULL**

- 조건식
  - 기본 CASE 식
  - 단순 CASE 식
  - COALESCE : 하나씩 조회해서 null이 아니면 반환
  - NULLIF: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환

- JPQL 함수
  - **JPQL 기본 함수**
    - jpql이 제공하는 표준함수
    - sql 종류에 상관없이 사용이 가능
    - CONCAT
    - SUBSTRING
    - TRIM
    - LOWER, UPPER
    - LENGTH
    - LOCATE
    - ABS, SQRT, MOD
    - SIZE, INDEX(JPA용도)
  - **사용자 정의 함수 호출**
    - 하이버네이트는 사용전 방언에 추가해야 한다.
    - 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다.
  - DB 종속 함수

- 경로 표현식
  - .(점)을 찍어 객체 그래프를 탐색하는 것
  ```
  select m.username -> 상태 필드 
  from Member m
  join m.team t   -> 단일 값 연관 필드
  join m.orders o -> 컬렉션 값 연관 필드
  where t.name = 'teamA' 
  ```
  - [**경로 표현식 용어 정리**]
    - **상태 필드(state field) :** 단순히 값을 저장하기 위한 필드 **(ex: m.username)**
    - **연관 필드(association field)**: 연관관계를 위한 필드
    - 단일 값 연관 필드 : @ManyToOne, @OneToOne, 대상이 엔티티**(ex: m.team)**
    - 컬렉션 값 연관 필드 : @OneToMany, @ManyToMany, 대상이 컬렉션**(ex: m.orders)**
    
  - [**경로 표현식 특징**]
    - **상태 필드**(state field) : 경로 탐색의 끝, 탐색 X
    - **단일 값 연관 경로** : **묵시적 내부 조인(inner join)** 발생, 탐색 O
    - **컬렉션 값 연관 경로** : **묵시적 내부 조인 발생**, 탐색 X
    - FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
    
  - [명시직 조인, 묵시적 조인]
    - 명시적 조인: jpql내 join 키워드 직접 사용
    - 묵시적 조인: 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능)
    
  - [경로 탐색을 사용한 묵시적 조인 시 주의사항]
    - 항상 내부 조인
    - 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야함
    - 경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만 **묵시적 조인으로 인해 SQL의 FROM (JOIN) 절에 영향을 줌**
    
  - [**실무 조언**]
    - ****가급적 묵시적 조인 대신에 명시적 조인 사용, 왠만하면 쓰지말자!!!****
    - 조인은 SQL 튜닝에 중요 포인트
    - 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움

- 페치 조인(fetch join)
  - **실무에서 정말 중요**
  - SQL 조인 종류 X
  - JPQL에서 **성능 최적화**를 위해 제공하는 기능
  - 연관된 엔티티나 컬렉션을 **SQL 한 번에 함께 조회**하는 기능
  - join fetch 명령어 사용
  - 페치 조인 ::= [ LEFT [OUTER] | INNER ] JOIN FETCH 조인경로
  - 엔티티 페치 조인
  - 컬렉션 페치 조인
  
  - [페치 조인과 DISTINCT]
    - SQL의 DISTINCT는 중복된 결과를 제거하는 명령
    - JPQL의 DISTINCT 2가지 기능 제공
      - **1. SQL에 DISTINCT를 추가**
      - **2. 애플리케이션에서 엔티티 중복 제거**
    - SQL에 DISTINCT를 추가하지만 데이터가 다르므로 SQL 결과에서 중복제거 실패
    - DISTINCT가 추가로 애플리케이션에서 중복 제거시도
  - [페치 조인과 일반 조인의 차이]
    - 일반 조인 실행 시 연관된 엔티티를 함께 조회하지 않음(지연로딩)
    - **JPQL은 결과를 반환할 때 연관관계 고려 X**
    - 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
    - **여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회X**
    - ****페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시 로딩)****
    - ****페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념****
    - **페치 조인은 연관된 엔티티를 함께 조회함**
  - [페치 조인의 특징과 한계]
    - **페치 조인 대상에는 별칭을 줄 수 없다. : 사용하지 말자**
      - 페치 조인이란 것이 나랑 연관된 모든것을 가져오는 것이다. → 객체 그래프를 탐색한다는 것은 모든것을 다 가져오는 것이다.
      - 하이버네이트는 가능, 가급적 사용 X
    - **둘 이상의 컬렉션은 페치 조인 할 수 없다.**
    - **컬렉션을 페치 조인하면 페이징 API(setFirstResult,setMaxResults)를 사용할 수 없다. : 일대다(데이터 뻥튀기),절대 사용하지 말자**
      - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
      - **하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)**
        - **`WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!`**
    - 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화(N + 1)
    - 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
      - **`@OneToMany(fetch = FetchType.LAZY)` // 글로벌 로딩 전략**
    - 실무에서 글로벌 로딩 전략은 모두 지연 로딩
    - 최적화가 필요한 곳은 페치 조인 적용
  - [정리]
    - 모든 것을 페치 조인으로 해결할 수 는 없음
    - 페치조인은 **객체 그래프를 유지** 할 때 사용하면 효과적
    - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면, 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적

- 다형성 쿼리
  - 조회 대상을 특정 자식으로 한정
    - 예) Item 중에 Book(extends Item), Movie(extends Item)를 조회해라
  - **TREAT(JPA 2.1)**
    - 자바의 타입 캐스팅과 유사
    - 상속구조에서 부모타입을 특정 자식타입으로 다룰 때 사용
    - FROM, WHERE, SELECT(하이버네이트 지원) 사용

- 엔티티 직접사용
  - 엔티티 직접 사용 : 기본 키 값
    - JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용
  - 엔티티 직접 사용 : 외래 키 값
     
- Named 쿼리
  - 미리 정의해서 이름을 부여해두고 사용하는 JPQL
  - 정적쿼리
  - 어노테이션 또는 XML에 정의
  - 애플리케이션 로딩 시점에 초기화 후 재사용
    - app 로딩시점에 jpql → sql 파싱 및 변환하여 캐쉬(성능상 이점)
  - **애플리케이션 로딩 시점에 쿼리를 검증**
  - **Named 쿼리 환경에 따른 설정**
    - XML이 항상 우선권을 가진다.
    - 애플리케이션 운영 환경에 따라 다른 XML을 배포할 수 있다.
  - Spring date JPA를 사용하면 @Query 어노테이션을 사용하는게 이게 이름없는 Named Query로 등록된다.
    - app 로딩 시점에 파싱하여 쿼리를 검증

- 벌크연산
  - ex) 재고가 10개 미만인 모든 상품의 가격을 10% 상승하려면 ?
    - JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL 실행
    - 1. 재고가 10개 미만인 상품을 리스트로 조회
    - 2. 상품 엔티티의 가격을 10% 증가
    - 3. 트랜잭션 커밋 시점에 변경감지가 동작한다.
    - 변경된 데이터가 100건이라면 100번의 UPDATE SQL 실행
  - 쿼리 한 번으로 여러 테이블 로우 변경(엔티티)
  - executeUpdate()의 결과는 영향받은 엔티티 수 반환
  - UPDATE, DELETE 지원
  - INSERT(insert into .. select, 하이버네이트 지원)
  - 벌크 연산 주의
    - 벌크 연산은 **영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리**
      - 벌크 연산을 먼저 실행
      - 벌크 연산 수행 후 영속성 컨텍스트 초기화(em.clear())