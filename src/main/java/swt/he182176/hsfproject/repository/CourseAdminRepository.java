package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseAdminRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.category")
    List<Course> findAllWithDetails();

    List<Course> findByPublishedTrue();

    long countByPublished(boolean published);

    List<Course> findTop5ByOrderByCreateAtDesc();
}
