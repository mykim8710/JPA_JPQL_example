package hello.jpa.jpql;

import hello.jpa.jpql.entity.Member;

import javax.persistence.*;
import java.util.List;

public class JpqlMainParameterBinding {
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

            Member member2 = new Member();
            member2.setUsername("name2");
            member2.setAge(22);
            em.persist(member2);

            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                                    .setParameter("username", "name2")
                                    .getSingleResult();
            System.out.println("singleResult.getUsername() = " + singleResult.getUsername());

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
