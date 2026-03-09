package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(int id);

    Course updateCourse(Course course);

    List<Course> getPublishedCourses();

    void deleteCourse(int id);

}