package hello.jpa.jpql;

import hello.jpa.jpql.dto.MemberDto;
import hello.jpa.jpql.entity.Address;
import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainProjectionSelect {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpql");
        // EntityManagerFactory app 로딩시점에 딱 하나만 만든다.

        EntityManager em = emf.createEntityManager();
        // 트랜젝션 단위(db 커넥션을 얻어서 쿼리를 날리고 결과를 얻는)로 EntityManager를 만들어야줘야 한다.
        // 쉽게 생각하면 db 커넥션을 하나 얻은 것이다.

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // db 트랜젝션을 시작

        try {
            Member member = new Member();
            member.setUsername("name");
            member.setAge(20);
            em.persist(member);

            em.flush();
            em.clear();

            // 엔티티 프로젝션
            // select m from Member m
            // 이때 컬렉션의 Member는 영속성 컨텍스트에서 관리한다.
//            List<Member> findMemberList = em.createQuery("select m from Member m", Member.class)
//                                            .getResultList();
//
//            Member findMember = findMemberList.get(0);
//            findMember.setAge(30);  // update 쿼리가 나감

            // 엔티티 프로젝션
            // SELECT m.team FROM Member m
//            List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
//                                      .getResultList();
//
//            List<Team> resultList2 = em.createQuery("select t from Member m join m.team t", Team.class)
//                                       .getResultList(); // 이렇게 사용하는것이 맞다(sql과 최대한 같게 사용 : join을 명시)

            // 임베디드 타입 프로젝션
            // SELECT o.address FROM Order o
//            List<Address> resultList = em.createQuery("SELECT o.address FROM Order o", Address.class)
//                                          .getResultList();

            // 스칼라 타입 프로젝션
            // SELECT m.username, m.age FROM Member m
            // 1. 쿼리타입으로 조회
            List resultList = em.createQuery("SELECT m.username, m.age FROM Member m").getResultList();
            Object o = resultList.get(0);
            Object[] result = (Object[])o;
            System.out.println("result[0] = " + result[0]);
            System.out.println("result[1] = " + result[1]);
            
            // 2. Object[] 타입으로 조회
            List<Object[]> resultList2 = em.createQuery("SELECT m.username, m.age FROM Member m").getResultList();
            Object[] objects = resultList2.get(0);
            System.out.println("objects[0] = " + objects[0]);
            System.out.println("objects[1] = " + objects[1]);

            // 3. new 명령어로 조회
            List<MemberDto> resultList3 = em.createQuery("SELECT new hello.jpa.jpql.dto.MemberDto(m.username, m.age) FROM Member m", MemberDto.class).getResultList();
            MemberDto memberDto = resultList3.get(0);
            System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
            System.out.println("memberDto.getAge() = " + memberDto.getAge());

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
