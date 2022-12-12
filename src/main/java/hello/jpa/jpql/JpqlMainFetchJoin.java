package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.*;
import java.util.List;

public class JpqlMainFetchJoin {
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

            // 엔티티 페치 조인
            String query = "select m from Member m join fetch m.team";
            List<Member> resultList = em.createQuery(query, Member.class).getResultList();
            for (Member member : resultList) {
                System.out.println("member name = " + member.getUsername() + " | team name = " + member.getTeam().getName());
            }

            // 컬렉션 페치 조인
            String query2 = "select t from Team t join fetch t.members";
            List<Team> resultList2 = em.createQuery(query2, Team.class).getResultList();
            for (Team team : resultList2) {
                System.out.println("team = " + team.getName() + " | members size = " +team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            // 페치 조인과 DISTINCT(SQL DISTINCT)
            String query3 = "select distinct t from Team t join fetch t.members";
            List<Team> resultList3 = em.createQuery(query3, Team.class).getResultList();
            for (Team team : resultList3) {
                System.out.println("team = " + team.getName() + " | members size = " +team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            // 일반조인
            String query4 = "select t from Team t join t.members";
            List<Team> resultList4 = em.createQuery(query4, Team.class).getResultList();
            for (Team team : resultList4) {
                System.out.println("team = " + team.getName() + " | members size = " +team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
