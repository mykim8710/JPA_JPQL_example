package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;

import javax.persistence.*;
import java.util.List;

public class JpqlMainPagination {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpql");
        // EntityManagerFactory app 로딩시점에 딱 하나만 만든다.

        EntityManager em = emf.createEntityManager();
        // 트랜젝션 단위(db 커넥션을 얻어서 쿼리를 날리고 결과를 얻는)로 EntityManager를 만들어야줘야 한다.
        // 쉽게 생각하면 db 커넥션을 하나 얻은 것이다.

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // db 트랜젝션을 시작

        try {
            for(int i =0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("test" +i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                                        .setFirstResult(10)
                                        .setMaxResults(10)
                                        .getResultList();
            System.out.println("resultList.size() = " + resultList.size());

            for (Member findMember : resultList) {
                System.out.println("findMember = " + findMember);
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
