package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query("""
        select distinct e.course
        from Enrollment e
        where e.user.id = :userId
          and upper(e.status) = 'APPROVED'
        order by e.course.createAt desc
    """)
    List<Course> findApprovedCoursesByUserId(@Param("userId") int userId);

    @Query("""
        select e
        from Enrollment e
        where e.user.id = :userId
          and upper(e.status) = 'APPROVED'
        order by e.registeredAt desc
    """)
    List<Enrollment> findApprovedEnrollmentsByUserId(@Param("userId") int userId);

    boolean existsByUser_IdAndCourse_CourseId(Integer userId, Integer courseId);

    boolean existsByUser_IdAndCourse_CourseIdAndStatusIgnoreCase(Integer userId,
                                                                 Integer courseId,
                                                                 String status);

    List<Enrollment> findByUserEmail(String email);

    List<Enrollment> findByCourse_Instructor_Id(Integer instructorId);

    List<Enrollment> findByUser_Id(Integer userId);

    Optional<Enrollment> findByUser_IdAndCourse_CourseId(Integer userId, Integer courseId);

    @Query("""
    SELECT e FROM Enrollment e 
    WHERE (:courseId IS NULL OR e.course.courseId = :courseId)
      AND (:userId IS NULL OR e.user.id = :userId)
      AND (:status IS NULL OR :status = '' OR upper(e.status) = upper(:status))
      AND (:keyword IS NULL OR :keyword = '' 
           OR upper(e.fullName) LIKE upper(concat('%', :keyword, '%'))
           OR upper(e.email) LIKE upper(concat('%', :keyword, '%'))
           OR upper(e.course.title) LIKE upper(concat('%', :keyword, '%')))
    ORDER BY e.registeredAt DESC
""")
    List<Enrollment> filterEnrollments(
            @Param("courseId") Integer courseId,
            @Param("userId") Integer userId,
            @Param("status") String status,
            @Param("keyword") String keyword);
}