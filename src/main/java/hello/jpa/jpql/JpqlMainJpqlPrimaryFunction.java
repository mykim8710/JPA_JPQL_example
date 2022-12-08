package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainJpqlPrimaryFunction {
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
            member.setType(MemberType.USER);
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
            member3.setType(MemberType.ADMIN);
            member3.changeTeam(team);
            em.persist(member3);

            em.flush();
            em.clear();

            // 기본함수 : CONCAT
            String queryConcat = "select concat('a', 'b') from Member m";
            String result = em.createQuery(queryConcat, String.class)
                                .getSingleResult();
            System.out.println("result = " + result);

            String queryConcat2 = "select 'a' || 'b' from Member m"; // 하이버네이트 구현체 문법
            String result2 = em.createQuery(queryConcat2, String.class)
                                .getSingleResult();
            System.out.println("result2 = " + result2);

            // 기본함수 : SUBSTRING
            String querySubString = "select substring('test',1, 2) from Member m";
            String result3 = em.createQuery(querySubString, String.class)
                                .getSingleResult();
            System.out.println("result3 = " + result3);

            // 기본함수 : LOCATE
            String queryLocate = "select locate('de', 'abcdefg') from Member m";
            Integer result4 = em.createQuery(queryLocate, Integer.class)
                                .getSingleResult();
            System.out.println("result4 = " + result4);

            // 기본함수 : SIZE(JPA용도)
            String querySize = "select size(t.members) from Team t";    // colletion의 size
            Integer result5 = em.createQuery(querySize, Integer.class)
                                .getSingleResult();
            System.out.println("result5 = " + result5);

            // 사용자 정의 함수 호출
            String queryUserDefinitionFunction = "select function('group_concat', m.username) from Member m";
            String result6 = em.createQuery(queryUserDefinitionFunction, String.class)
                                .getSingleResult();
            System.out.println("result6 = " + result6);

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
