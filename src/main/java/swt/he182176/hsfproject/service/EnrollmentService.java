package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.dto.EnrollRequest;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.entity.User;

import java.util.List;

public interface EnrollmentService {

    Enrollment enrollCourse(Integer userId, Integer courseId);

    boolean isEnrolled(Integer userId, Integer courseId);

    Enrollment createEnrollment(EnrollRequest request, User user);

    List<Enrollment> getMyEnrollments(Integer userId);

    List<Enrollment> getAllEnrollments();

    Enrollment getEnrollmentById(Integer id);

    void approveEnrollment(Integer id);

    void rejectEnrollment(Integer id, String note);

    void deleteEnrollment(Integer id);

    Enrollment findById(int id);

    void save(Enrollment enrollment);
}