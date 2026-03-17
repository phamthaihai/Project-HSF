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
        and e.status = 'APPROVED'
        order by e.registeredAt desc
    """)
        // Đổi int thành Integer để an toàn hơn
    List<Course> findApprovedCoursesByUserId(@Param("userId") Integer userId);

    // Phương thức này đã dùng Integer, rất tốt
    boolean existsByUser_IdAndCourse_CourseId(Integer userId, Integer courseId);

    List<Enrollment> findByUserEmail(String email);

    List<Enrollment> findByCourseInstructorId(Integer instructorId);

    List<Enrollment> findByUser_Id(Integer userId);
}