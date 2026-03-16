package swt.he182176.hsfproject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByPostId(Integer postId);

    long countByStatus(String status);

    @Query("""
        SELECT p
        FROM Post p
        WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:category IS NULL OR p.type = :category)
          AND (:author IS NULL OR LOWER(p.user.fullName) LIKE LOWER(CONCAT('%', :author, '%')))
        ORDER BY p.updatedAt DESC
    """)
    List<Post> searchPosts(@Param("keyword") String keyword,
                           @Param("category") String category,
                           @Param("author") String author);

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.status = 'Published'
          AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:category IS NULL OR p.type = :category)
        ORDER BY p.updatedAt DESC
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
        SELECT DISTINCT p.user.fullName
        FROM Post p
        WHERE p.user IS NOT NULL
          AND p.user.fullName IS NOT NULL
          AND TRIM(p.user.fullName) <> ''
        ORDER BY p.user.fullName
    """)
    List<String> findAllAuthors();

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.status = 'Published'
        ORDER BY p.createdAt DESC
    """)
    List<Post> findPublishedPosts(Pageable pageable);

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.postId = :id
          AND p.status = 'Published'
    """)
    Optional<Post> findPublishedById(@Param("id") Integer id);

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.status = 'Published'
          AND p.type = :category
          AND p.postId <> :currentId
        ORDER BY p.updatedAt DESC
    """)
    List<Post> findRelatedPublishedBlogs(@Param("currentId") Integer currentId,
                                         @Param("category") String category);
}