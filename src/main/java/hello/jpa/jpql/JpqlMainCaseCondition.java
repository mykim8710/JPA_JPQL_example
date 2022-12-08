package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainCaseCondition {
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
            team.setName("aTeam");
            em.persist(team);

            Member member = new Member();
            member.setUsername("user");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.changeTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername(null);
            member2.setAge(30);
            member2.setType(MemberType.USER);
            member2.changeTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("관리자");
            member3.setAge(30);
            member3.setType(MemberType.USER);
            member3.changeTeam(team);
            em.persist(member3);

            em.flush();
            em.clear();

            // 단순 case
            String query = "select " +
                            "case when m.age <= 10 then '학생요금' " +
                                 "when m.age >= 60 then '경로요금' " +
                                 "else '일반요금' end "+
                            "from Member m";
            List<String> resultList = em.createQuery(query, String.class)
                                            .getResultList();

            for (String result : resultList) {
                System.out.println("result = " + result);
            }

            // COALESCE : 하나씩 조회해서 null이 아니면 반환
            String query2 = "select coalesce(m.username,'이름 없는 회원') from Member m";
            List<String> resultList2 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String result : resultList2) {
                System.out.println("result = " + result);
            }

            // NULLIF: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
            String query3 = "select NULLIF(m.username, '관리자') from Member m";
            List<String> resultList3 = em.createQuery(query3, String.class)
                    .getResultList();

            for (String result : resultList3) {
                System.out.println("result = " + result);
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
