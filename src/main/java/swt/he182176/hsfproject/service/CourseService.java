package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.dto.MyCourseCardDTO;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;

import java.util.List;

public interface CourseService {
    List<Course> getPublicCourses(String keyword, Integer categoryId);
    List<MyCourseCardDTO> getMyCourses(User loginUser);
    Course getPublicCourseDetail(Integer courseId);
}