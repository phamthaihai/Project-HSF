package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseAdminRepository extends JpaRepository<Course, Integer> {

    List<Course> findByPublishedTrue();

    long countByPublished(boolean published);

    List<Course> findTop5ByOrderByCreateAtDesc();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.category")
    List<Course> findAllWithDetails();

    List<Course> findTop8ByPublishedTrueOrderByCreateAtDesc();
}