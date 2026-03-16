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
    public List<Course> getPublicCourses(String keyword) {
        String kw = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        return courseRepository.findPublicCourses(kw);
    }

    @Override
    public List<Course> getMyCourses(int userId) {
        return enrollmentRepository.findApprovedCoursesByUserId(userId);
    }
}