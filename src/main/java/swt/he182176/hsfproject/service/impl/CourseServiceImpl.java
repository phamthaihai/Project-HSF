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
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

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

        List<MyCourseCardDTO> result = new ArrayList<>();

        if ("ADMIN".equals(roleName)) {
            List<Course> courses = courseRepository.findByPublishedTrueOrderByCreateAtDesc();
            for (Course course : courses) {
                result.add(toMyCourseCardDTO(course));
            }
        } else if ("MANAGER".equals(roleName)) {
            List<Course> courses = courseRepository.findByInstructor_IdOrderByCreateAtDesc(loginUser.getId());
            for (Course course : courses) {
                result.add(toMyCourseCardDTO(course));
            }
        } else if ("MEMBER".equals(roleName)) {
            List<swt.he182176.hsfproject.entity.Enrollment> enrollments = enrollmentRepository.findApprovedEnrollmentsByUserId(loginUser.getId());
            for (swt.he182176.hsfproject.entity.Enrollment enrollment : enrollments) {
                MyCourseCardDTO dto = toMyCourseCardDTO(enrollment.getCourse());
                dto.setEnrolledDate(enrollment.getRegisteredAt());
                dto.setProgressPercent(0.0);
                dto.setProgressStatus("In Progress");
                result.add(dto);
            }
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