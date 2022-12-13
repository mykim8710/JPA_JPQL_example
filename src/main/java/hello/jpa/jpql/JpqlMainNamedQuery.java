package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainNamedQuery {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpql");
        // EntityManagerFactory app 로딩시점에 딱 하나만 만든다.

        EntityManager em = emf.createEntityManager();
        // 트랜젝션 단위(db 커넥션을 얻어서 쿼리를 날리고 결과를 얻는)로 EntityManager를 만들어야줘야 한다.
        // 쉽게 생각하면 db 커넥션을 하나 얻은 것이다.

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // db 트랜젝션을 시작

        try {
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setType(MemberType.ADMIN);
            member1.setUsername("member1");
            member1.setAge(10);
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setType(MemberType.ADMIN);
            member2.setUsername("member2");
            member2.setAge(15);
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setType(MemberType.ADMIN);
            member3.setUsername("member3");
            member3.setAge(20);
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // Named Query : @annotation
            Member findMember = em.createNamedQuery("Member.findByUsername", Member.class)
                                .setParameter("username", "member3")
                                .getSingleResult();
            System.out.println("findMember = " + findMember);

            // Named Query : XML
            Member findMember2 = em.createNamedQuery("Member.findByUsername2", Member.class)
                                    .setParameter("username", "member1")
                                    .getSingleResult();
            System.out.println("findMember2 = " + findMember2);

            Object count = em.createNamedQuery("Member.count").getSingleResult();
            System.out.println("singleResult = " + count);

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
