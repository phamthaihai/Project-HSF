package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseAdminRepository extends JpaRepository<Course, Integer> {
    List<Course> findByPulishedTrue();}
