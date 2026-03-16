package swt.he182176.hsfproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.repository.CourseRepository;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public List<Course> getPublicCourses(String keyword, Integer categoryId) {
        String kw = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        return courseRepository.findPublicCourses(kw, categoryId);
    }

    @Override
    public Course getPublicCourseDetail(Integer courseId) {
        return courseRepository.findById(courseId)
                .filter(Course::isPublished)
                .orElseThrow(() -> new RuntimeException("Course not found or not published"));
    }

    @Override
    public List<Course> getMyCourses(int userId) {
        return enrollmentRepository.findApprovedCoursesByUserId(userId);
    }
}