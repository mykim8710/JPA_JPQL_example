package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;
import hello.jpa.jpql.entity.MemberType;
import hello.jpa.jpql.entity.Team;

import javax.persistence.*;
import java.util.List;

public class JpqlMainJpqlType {
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

            em.flush();
            em.clear();

            String query = "select m.username, m.type, 'HELLO', true from Member m where m.type = :memberType";
            List<Object[]> resultList = em.createQuery(query)
                                            .setParameter("memberType", MemberType.ADMIN)
                                            .getResultList();

            Object[] objects = resultList.get(0);
            for (int i =0; i< objects.length; i++) {
                System.out.println("objects : " + objects[i]);
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
