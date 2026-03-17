package swt.he182176.hsfproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swt.he182176.hsfproject.dto.EnrollRequest;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.CourseRepository;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.service.EnrollmentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;



    @Override
    public Enrollment enrollCourse(Integer userId, Integer courseId) {

        boolean exists =
                enrollmentRepository.existsByUser_IdAndCourse_CourseId(userId, courseId);

        if (exists) {
            throw new RuntimeException("User already enrolled this course");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Enrollment enrollment = new Enrollment();

        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setStatus("PENDING");
        enrollment.setRegisteredAt(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public boolean isEnrolled(Integer userId, Integer courseId) {

        return enrollmentRepository
                .existsByUser_IdAndCourse_CourseId(userId, courseId);
    }

    @Override
    public Enrollment createEnrollment(EnrollRequest request, User user) {

        if (user == null) {
            throw new RuntimeException("User must login before enrolling");
        }

        Integer courseId = request.getCourseId();

        // 1. Thay vì dùng exists, ta tìm bản ghi cũ (nếu có)
        Optional<Enrollment> existingEnrollment =
                enrollmentRepository.findByUser_IdAndCourse_CourseId(user.getId(), courseId);

        if (existingEnrollment.isPresent()) {
            Enrollment current = existingEnrollment.get();

            // 2. Nếu đã thanh toán thành công (APPROVED), lúc này mới báo lỗi chặn lại
            if ("APPROVED".equalsIgnoreCase(current.getStatus())) {
                throw new RuntimeException("User already enrolled this course");
            }

            // 3. Nếu đang PENDING, ta cập nhật lại thông tin mới nhất từ form
            // (Ví dụ: người dùng đổi số điện thoại hoặc đổi phương thức thanh toán từ PayOS sang VNPay)
            current.setFullName(request.getFullName());
            current.setEmail(request.getEmail());
            current.setMobile(request.getMobile());
            current.setNote(request.getNote());
            current.setPaymentMethod(request.getPaymentMethod());
            current.setUpdatedAt(LocalDateTime.now()); // Đảm bảo bạn có trường này để theo dõi

            // Trả về bản ghi cũ đã cập nhật để Controller dẫn đi thanh toán tiếp
            return enrollmentRepository.save(current);
        }

        // 4. Nếu chưa tồn tại bản ghi nào, tiến hành tạo mới như bình thường
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setFullName(request.getFullName());
        enrollment.setEmail(request.getEmail());
        enrollment.setMobile(request.getMobile());
        enrollment.setNote(request.getNote());
        enrollment.setPaymentMethod(request.getPaymentMethod());

        enrollment.setStatus("PENDING");
        enrollment.setRegisteredAt(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getMyEnrollments(Integer userId) {

        return enrollmentRepository.findByUser_Id(userId);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {

        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Integer id) {

        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }

    @Override
    public void approveEnrollment(Integer id) {

        Enrollment enrollment = getEnrollmentById(id);

        enrollment.setStatus("APPROVED");
        enrollment.setUpdatedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

    @Override
    public void rejectEnrollment(Integer id, String note) {

        Enrollment enrollment = getEnrollmentById(id);

        enrollment.setStatus("REJECTED");
        enrollment.setRejectedNote(note);
        enrollment.setUpdatedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Integer id) {

        enrollmentRepository.deleteById(id);
    }
    @Override
    public void save(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
    }
    @Override
    public Enrollment findById(int id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }
    @Override
    public List<Enrollment> filterEnrollments(Integer courseId, Integer userId, String status, String keyword) {
        // Gọi sang Repository đã được thêm @Query mà tôi hướng dẫn ở trước
        return enrollmentRepository.filterEnrollments(courseId, userId, status, keyword);
    }
}