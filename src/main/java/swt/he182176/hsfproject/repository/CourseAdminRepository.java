package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseAdminRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.category")
    List<Course> findAllWithDetails();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.category ORDER BY c.createAt DESC")
    List<Course> findTop5ByOrderByCreateAtDescWithDetails();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.category WHERE c.published = true ORDER BY c.createAt DESC")
    List<Course> findTop8ByPublishedTrueOrderByCreateAtDescWithDetails();

    List<Course> findByPublishedTrue();

    long countByPublished(boolean published);

    List<Course> findTop5ByOrderByCreateAtDesc();

    List<Course> findTop8ByPublishedTrueOrderByCreateAtDesc();
}
