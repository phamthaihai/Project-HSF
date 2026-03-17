package swt.he182176.hsfproject.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.repository.CourseAdminRepository;
import swt.he182176.hsfproject.repository.PostRepository;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.repository.EnrollmentRepository; // Cần import thêm cái này
import swt.he182176.hsfproject.service.CourseAdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseAdminServiceImpl implements CourseAdminService {

    private final CourseAdminRepository courseAdminRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final EnrollmentRepository enrollmentRepository; // Thêm repo này để xóa enrollment

    public CourseAdminServiceImpl(
            CourseAdminRepository courseAdminRepository,
            UserRepository userRepository,
            PostRepository postRepository,
            EnrollmentRepository enrollmentRepository) { // Inject vào constructor

        this.courseAdminRepository = courseAdminRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseAdminRepository.findAllWithDetails();
    }

    @Override
    public Course getCourseById(int id) {
        return courseAdminRepository.findById(id).orElse(null);
    }

    @Override
    public Course updateCourse(Course course) {
        if (course.getSalePrice() != null && course.getSalePrice() > 0) {
            course.setPrice(course.getPrice() - course.getSalePrice());
            if (course.getPrice() < 0) course.setPrice(0);
        }
        return courseAdminRepository.save(course);
    }

    @Override
    public List<Course> getPublishedCourses() {
        return courseAdminRepository.findByPublishedTrue();
    }

    @Override
    public List<Course> getTop8PublishedCourses() {
        return courseAdminRepository.findTop8ByPublishedTrueOrderByCreateAtDescWithDetails();
    }


    @Override
    @Transactional
    public void deleteCourse(int id) {
        if (courseAdminRepository.existsById(id)) {
            Course course = courseAdminRepository.findById(id).get();
            enrollmentRepository.deleteByCourse(course);
            courseAdminRepository.delete(course);
            System.out.println("Xóa thành công khóa học ID: " + id);
        } else {

            System.out.println("Không tìm thấy khóa học ID: " + id + " để xóa.");
        }
    }

    @Override
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalCourses = courseAdminRepository.count();
        long publishedCourses = courseAdminRepository.countByPublished(true);
        long publishedPosts = postRepository.countByStatus("PUBLISHED");

        List<Course> recentCourses =
                courseAdminRepository.findTop5ByOrderByCreateAtDescWithDetails();

        data.put("totalUsers", totalUsers);
        data.put("totalCourses", totalCourses);
        data.put("publishedCourses", publishedCourses);
        data.put("publishedPosts", publishedPosts);
        data.put("recentCourses", recentCourses);

        return data;
    }
}