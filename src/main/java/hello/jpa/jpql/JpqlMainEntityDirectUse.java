package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainEntityDirectUse {
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

            // 엔티티 직접 사용 - 기본 키 값
            // COUNT : 엔티티를 직접 사용
            String query = "select count(m) from Member m";
            Object singleResult1 =  em.createQuery(query).getSingleResult();
            System.out.println("count = " + singleResult1);

            // COUNT : 엔티티의 아이디를 사용
            String query2 = "select count(m.id) from Member m";
            Object singleResult2 = em.createQuery(query2).getSingleResult();
            System.out.println("count = " + singleResult2);
                    
            // 엔티티를 파라미터로 전달
            String query3 = "select m from Member m where m = :member";
            Member findMember = em.createQuery(query3, Member.class).setParameter("member", member1).getSingleResult();
            System.out.println("findMember = " + findMember);

            // 식별자를 직접 전달
            String query4 = "select m from Member m where m.id = :memberId";
            Member findMember2 = em.createQuery(query4, Member.class).setParameter("memberId", member2.getId()).getSingleResult();
            System.out.println("findMember2 = " + findMember2);


            // 엔티티 직접 사용 - 외래 키 값
            // 엔티티를 파라미터로 전달
            String query5 = "select m from Member m where m.team = :team";
            List<Member> findMembers = em.createQuery(query5, Member.class)
                                        .setParameter("team", teamA)
                                        .getResultList();
            System.out.println("findMembers = " + findMembers);

            // 식별자를 직접 전달
            String query6 = "select m from Member m where m.team.id = :teamId";
            List<Member> findMembers2 = em.createQuery(query6, Member.class)
                                            .setParameter("teamId", teamA.getId())
                                            .getResultList();
            System.out.println("findMembers2 = " + findMembers2);

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
