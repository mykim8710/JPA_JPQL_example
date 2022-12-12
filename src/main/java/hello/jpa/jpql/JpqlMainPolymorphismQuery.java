package hello.jpa.jpql;

import hello.jpa.jpql.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMainPolymorphismQuery {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpql");
        // EntityManagerFactory app 로딩시점에 딱 하나만 만든다.

        EntityManager em = emf.createEntityManager();
        // 트랜젝션 단위(db 커넥션을 얻어서 쿼리를 날리고 결과를 얻는)로 EntityManager를 만들어야줘야 한다.
        // 쉽게 생각하면 db 커넥션을 하나 얻은 것이다.

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // db 트랜젝션을 시작

        try {
            Book book = new Book();
            book.setName("book");
            book.setIsbn("111");
            book.setAuthor("kim");
            book.setPrice(1111);
            em.persist(book);

            Album album = new Album();
            album.setArtist("aa");
            album.setPrice(222);
            album.setName("album");
            em.persist(album);

            Movie movie = new Movie();
            movie.setActor("actor");
            movie.setDirector("director");
            movie.setPrice(222);
            movie.setName("movie");
            em.persist(movie);

            em.flush();
            em.clear();

            String query = "select i from Item i where type(i) in(Movie, Book)";
            List<Item> resultList = em.createQuery(query, Item.class).getResultList();
//            for (Item item : resultList) {
//                System.out.println("item.getName() = " + item.getName());
//            }

            // TREAT(JPA 2.1)
            String queryTreat = "select i from Item i where treat(i as Book ).author = 'kim'";
            List<Item> resultList2 = em.createQuery(queryTreat, Item.class).getResultList();


            tx.commit();    // 커밋
        }catch (Exception e) {
            tx.rollback();  // 롤백
        } finally {
            em.close();
        }

        emf.close();
    }
}
