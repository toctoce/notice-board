package toctoce.notice_board.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toctoce.notice_board.domain.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findByPostId(long postId) {
        return em.createQuery("select c from Comment c where c.post = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
