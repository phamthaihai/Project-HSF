package swt.he182176.hsfproject.service.impl;

import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.repository.CourseRepository;
import swt.he182176.hsfproject.service.CourseAdminService;

import java.util.List;

@Service
public class CourseAdminServiceImpl implements CourseAdminService {

    private final CourseRepository courseRepository;

    public CourseAdminServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(int id) {
        return courseRepository.findById(id).orElse(null);
    }


    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getPublishedCourses() {
        return courseRepository.findByPublishedTrue();
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}