package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByPostId(int postId);

    @Query("""
        SELECT p
        FROM Post p
        WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:category IS NULL OR LOWER(p.type) = LOWER(:category))
          AND (:status IS NULL OR LOWER(p.status) = LOWER(:status))
        ORDER BY p.updatedAt DESC, p.postId DESC
    """)
    List<Post> searchPosts(@Param("keyword") String keyword,
                           @Param("category") String category,
                           @Param("status") String status);

    @Query("""
        SELECT p
        FROM Post p
        WHERE LOWER(p.status) = 'published'
          AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:category IS NULL OR LOWER(p.type) = LOWER(:category))
        ORDER BY p.updatedAt DESC, p.postId DESC
    """)
    List<Post> searchPublishedBlogs(@Param("keyword") String keyword,
                                    @Param("category") String category);

    @Query("""
        SELECT DISTINCT p.type
        FROM Post p
        WHERE p.type IS NOT NULL AND TRIM(p.type) <> ''
        ORDER BY p.type
    """)
    List<String> findAllCategories();

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.postId = :id AND LOWER(p.status) = 'published'
    """)
    Optional<Post> findPublishedById(@Param("id") Integer id);
}