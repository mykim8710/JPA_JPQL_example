package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainJoin {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpql");
        // EntityManagerFactory app 로딩시점에 딱 하나만 만든다.

        EntityManager em = emf.createEntityManager();
        // 트랜젝션 단위(db 커넥션을 얻어서 쿼리를 날리고 결과를 얻는)로 EntityManager를 만들어야줘야 한다.
        // 쉽게 생각하면 db 커넥션을 하나 얻은 것이다.

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // db 트랜젝션을 시작

        try {
            Team team = new Team();
            team.setName("team");
            em.persist(team);

            Member member = new Member();
            member.setUsername("test");
            member.setAge(22);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            // (inner) join
            String innerJoinQuery = "select m from Member m inner join m.team t where t.name = :teamName"; // inner 생략가능
            List<Member> resultList = em.createQuery(innerJoinQuery, Member.class)
                                        .setParameter("teamName", "team")
                                        .getResultList();

            // left (outer) join
            String leftOuterJoinQuery = "select m from Member m left outer join m.team t where t.name = :teamName"; // outer 생략가능
            List<Member> resultList2 = em.createQuery(leftOuterJoinQuery, Member.class)
                                        .setParameter("teamName", "team")
                                        .getResultList();

            // Seta join : 막조인
            String setaJoinQuery = "select m from Member m, Team t where m.username = t.name";
            List<Member> resultList3 = em.createQuery(setaJoinQuery, Member.class)
                                        .getResultList();


            // on절 join : 1. 조인 대상 필터링
            String onJoinQuery1 = "SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = :teamName";
            List<Member> resultList4 = em.createQuery(onJoinQuery1, Member.class)
                                        .setParameter("teamName", "test")
                                        .getResultList();

            // on절 join : 2. 연관관계 없는 엔티티 외부 조인
            String onJoinQuery2 = "SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name";
            List<Member> resultList5 = em.createQuery(onJoinQuery2, Member.class)
                                        .getResultList();

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
