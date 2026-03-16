package swt.he182176.hsfproject.service.impl;

import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.repository.CourseAdminRepository;
import swt.he182176.hsfproject.repository.PostRepository;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.service.CourseAdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseAdminServiceImpl implements CourseAdminService {

    private final CourseAdminRepository courseAdminRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CourseAdminServiceImpl(
            CourseAdminRepository courseAdminRepository,
            UserRepository userRepository,
            PostRepository postRepository) {

        this.courseAdminRepository = courseAdminRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
    public void deleteCourse(int id) {
        courseAdminRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getDashboardData() {

        Map<String, Object> data = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalCourses = courseAdminRepository.count();
        long publishedCourses = courseAdminRepository.countByPublished(true);
        long publishedPosts = postRepository.countByStatus("PUBLISHED");

        List<Course> recentCourses =
                courseAdminRepository.findTop5ByOrderByCreateAtDesc();

        data.put("totalUsers", totalUsers);
        data.put("totalCourses", totalCourses);
        data.put("publishedCourses", publishedCourses);
        data.put("publishedPosts", publishedPosts);
        data.put("recentCourses", recentCourses);

        return data;
    }
}