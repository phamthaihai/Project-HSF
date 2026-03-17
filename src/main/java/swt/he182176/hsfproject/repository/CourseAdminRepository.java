package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseAdminRepository extends JpaRepository<Course, Integer> {

    List<Course> findByPublishedTrue();

    long countByPublished(boolean published);

    List<Course> findTop5ByOrderByCreateAtDesc();
}