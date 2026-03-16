package swt.he182176.hsfproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.dto.MyCourseCardDTO;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.CourseRepository;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.CourseService;

import java.util.ArrayList;
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
    public List<MyCourseCardDTO> getMyCourses(User loginUser) {
        if (loginUser == null) {
            return new ArrayList<>();
        }

        String roleName = loginUser.getRole() != null && loginUser.getRole().getName() != null
                ? loginUser.getRole().getName().trim().toUpperCase()
                : "";

        List<Course> courses;

        if ("ADMIN".equals(roleName)) {
            courses = courseRepository.findByPublishedTrueOrderByCreateAtDesc();
        } else if ("MANAGER".equals(roleName)) {
            courses = courseRepository.findByInstructor_IdOrderByCreateAtDesc(loginUser.getId());
        } else if ("MEMBER".equals(roleName)) {
            courses = enrollmentRepository.findApprovedCoursesByUserId(loginUser.getId());
        } else {
            courses = new ArrayList<>();
        }

        List<MyCourseCardDTO> result = new ArrayList<>();
        for (Course course : courses) {
            result.add(toMyCourseCardDTO(course));
        }
        return result;
    }

    private MyCourseCardDTO toMyCourseCardDTO(Course course) {
        MyCourseCardDTO dto = new MyCourseCardDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setDuration(course.getDuration());
        dto.setThumbnailUrl(course.getThumbnailUrl());
        dto.setCategoryName(course.getCategory() != null ? course.getCategory().getName() : null);
        dto.setInstructorName(course.getInstructor() != null ? course.getInstructor().getFullName() : null);
        dto.setStartLearningUrl("/my-courses/" + course.getCourseId() + "/learn");
        return dto;
    }
}