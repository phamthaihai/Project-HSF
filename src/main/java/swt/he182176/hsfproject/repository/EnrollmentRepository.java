package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query("""
        select e.course
        from Enrollment e
        where e.user.id = :userId
          and upper(e.status) = 'APPROVED'
        order by e.registeredAt desc
    """)
    List<Course> findApprovedCoursesByUserId(@Param("userId") int userId);
}