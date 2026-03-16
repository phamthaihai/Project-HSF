package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getPublicCourses(String keyword, Integer categoryId);
    List<Course> getMyCourses(int userId);
    Course getPublicCourseDetail(Integer courseId);
}