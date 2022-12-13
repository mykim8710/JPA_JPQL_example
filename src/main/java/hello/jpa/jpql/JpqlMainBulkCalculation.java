package hello.jpa.jpql;

import hello.jpa.jpql.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpqlMainBulkCalculation {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpql");
        // EntityManagerFactory app 로딩시점에 딱 하나만 만든다.

        EntityManager em = emf.createEntityManager();
        // 트랜젝션 단위(db 커넥션을 얻어서 쿼리를 날리고 결과를 얻는)로 EntityManager를 만들어야줘야 한다.
        // 쉽게 생각하면 db 커넥션을 하나 얻은 것이다.

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // db 트랜젝션을 시작

        try {

            Product product = new Product();
            product.setPrice(100 * 1);
            product.setStockAmount(10 + 1);
            em.persist(product);

            Product product2 = new Product();
            product2.setPrice(100 * 2);
            product2.setStockAmount(10 + 2);
            em.persist(product2);

            Product product3 = new Product();
            product3.setPrice(100 * 3);
            product3.setStockAmount(10 + 3);
            em.persist(product3);

            Product product4 = new Product();
            product4.setPrice(100 * 4);
            product4.setStockAmount(10 + 4);
            em.persist(product4);

            Product product5 = new Product();
            product5.setPrice(100 * 5);
            product5.setStockAmount(10 + 5);
            em.persist(product5);

//            em.flush();
//            em.clear();

            String query = "update Product p " +
                            "set p.price = p.price * 1.1 " +
                            "where p.stockAmount <= :stockAmount";

            // 벌크연산 실행 : FLUSH 자동호출, DB에만 반영
            int result = em.createQuery(query)
                            .setParameter("stockAmount", 12)
                            .executeUpdate();
            System.out.println("result = " + result);

            em.clear(); // 영속성 컨텍스트 초기화

            Product findProduct = em.find(Product.class, product.getId());
            Product findProduct2 = em.find(Product.class, product2.getId());

            System.out.println("product price = " + findProduct.getPrice());
            System.out.println("product2 price = " + findProduct2.getPrice());

            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}

