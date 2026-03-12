package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
        select c
        from Course c
        where c.pulished = true
          and (
                :keyword is null
                or trim(:keyword) = ''
                or lower(c.title) like lower(concat('%', :keyword, '%'))
              )
        order by c.courseId desc
    """)
    List<Course> findPublicCourses(@Param("keyword") String keyword);
}