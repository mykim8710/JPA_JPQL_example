package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainFetchJoinLimit {
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

            Team teamC = new Team();
            teamC.setName("teamC");
            em.persist(teamC);

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

            Member member4 = new Member();
            member4.setType(MemberType.ADMIN);
            member4.setUsername("member4");
            member4.setAge(26);
            em.persist(member4);

            em.flush();
            em.clear();

            // 페치 조인의 특징과 한계
            // as tt(X) : 페치 조인 대상에는 별칭을 줄 수 없다.
            String query = "select t from Team t join fetch t.members";

            // 컬렉션을 페치 조인하면 페이징 API(setFirstResult,setMaxResults)를 사용할 수 없다. : 일대다(데이터 뻥튀기)
            List<Team> resultList = em.createQuery(query, Team.class)
                                        .setFirstResult(0)
                                        .setMaxResults(1)   // WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
                                        .getResultList();

            System.out.println("resultList = " + resultList.size());

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
