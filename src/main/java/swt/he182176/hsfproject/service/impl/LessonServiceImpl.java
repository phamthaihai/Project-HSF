package swt.he182176.hsfproject.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import swt.he182176.hsfproject.dto.ChapterItemDTO;
//import swt.he182176.hsfproject.dto.LessonContentDTO;
//import swt.he182176.hsfproject.dto.LessonItemDTO;
//import swt.he182176.hsfproject.dto.LessonViewerDTO;
//import swt.he182176.hsfproject.entity.Chapter;
//import swt.he182176.hsfproject.entity.Course;
//import swt.he182176.hsfproject.entity.Lesson;
//import swt.he182176.hsfproject.entity.User;
//import swt.he182176.hsfproject.repository.ChapterRepository;
//import swt.he182176.hsfproject.repository.CourseRepository;
//import swt.he182176.hsfproject.repository.EnrollmentRepository;
//import swt.he182176.hsfproject.repository.LessonRepository;
//import swt.he182176.hsfproject.service.LessonService;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@Transactional(readOnly = true)
//public class LessonServiceImpl implements LessonService {
//
//    @Autowired
//    private CourseRepository courseRepository;
//
//    @Autowired
//    private ChapterRepository chapterRepository;
//
//    @Autowired
//    private LessonRepository lessonRepository;
//
//    @Autowired
//    private EnrollmentRepository enrollmentRepository;
//
//    @Override
//    public LessonViewerDTO getLessonViewer(User loginUser, Integer courseId, Integer lessonId) {
//        if (loginUser == null) {
//            throw new RuntimeException("Please login to continue");
//        }
//
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//
//        if (!canAccessCourse(loginUser, course)) {
//            throw new RuntimeException("You do not have access to this course");
//        }
//
//        List<Chapter> chapters = chapterRepository
//                .findByCourse_CourseIdAndStatusIgnoreCaseOrderBySortOrderAsc(courseId, "ACTIVE");
//
//        List<Lesson> flatLessons = lessonRepository.findActiveLessonsByCourseId(courseId, "ACTIVE");
//
//        Lesson currentLesson = resolveCurrentLesson(lessonId, flatLessons);
//
//        LessonViewerDTO viewerDTO = new LessonViewerDTO();
//        viewerDTO.setCourseId(course.getCourseId());
//        viewerDTO.setCourseTitle(course.getTitle());
//        viewerDTO.setCourseDescription(course.getDescription());
//        viewerDTO.setCourseThumbnailUrl(course.getThumbnailUrl());
//
//        viewerDTO.setChapters(mapChapters(chapters, flatLessons, currentLesson));
//
//        if (currentLesson != null) {
//            viewerDTO.setCurrentLesson(toLessonContentDTO(currentLesson));
//
//            int currentIndex = indexOfLesson(flatLessons, currentLesson.getLessonId());
//            if (currentIndex > 0) {
//                viewerDTO.setPreviousLessonId(flatLessons.get(currentIndex - 1).getLessonId());
//            }
//            if (currentIndex >= 0 && currentIndex < flatLessons.size() - 1) {
//                viewerDTO.setNextLessonId(flatLessons.get(currentIndex + 1).getLessonId());
//            }
//        }
//
//        return viewerDTO;
//    }
//
//    private boolean canAccessCourse(User loginUser, Course course) {
//        String roleName = loginUser.getRole() != null && loginUser.getRole().getName() != null
//                ? loginUser.getRole().getName().trim().toUpperCase()
//                : "";
//
//        if ("ADMIN".equals(roleName)) {
//            return true;
//        }
//
//        if ("MANAGER".equals(roleName)) {
//            return course.getInstructor() != null
//                    && Objects.equals(course.getInstructor().getId(), loginUser.getId());
//        }
//
//        return enrollmentRepository.existsByUser_IdAndCourse_CourseIdAndStatusIgnoreCase(
//                loginUser.getId(),
//                course.getCourseId(),
//                "APPROVED"
//        );
//    }
//
//    private Lesson resolveCurrentLesson(Integer lessonId, List<Lesson> flatLessons) {
//        if (flatLessons == null || flatLessons.isEmpty()) {
//            return null;
//        }
//
//        if (lessonId == null) {
//            return flatLessons.get(0);
//        }
//
//        for (Lesson lesson : flatLessons) {
//            if (Objects.equals(lesson.getLessonId(), lessonId)) {
//                return lesson;
//            }
//        }
//
//        return flatLessons.get(0);
//    }
//
//    private List<ChapterItemDTO> mapChapters(List<Chapter> chapters, List<Lesson> flatLessons, Lesson currentLesson) {
//        List<ChapterItemDTO> result = new ArrayList<>();
//
//        for (Chapter chapter : chapters) {
//            ChapterItemDTO chapterDTO = new ChapterItemDTO();
//            chapterDTO.setChapterId(chapter.getChapterId());
//            chapterDTO.setTitle(chapter.getTitle());
//            chapterDTO.setSortOrder(chapter.getSortOrder());
//
//            List<LessonItemDTO> lessonItems = new ArrayList<>();
//            for (Lesson lesson : flatLessons) {
//                if (lesson.getChapter() != null
//                        && Objects.equals(lesson.getChapter().getChapterId(), chapter.getChapterId())) {
//
//                    LessonItemDTO lessonDTO = new LessonItemDTO();
//                    lessonDTO.setLessonId(lesson.getLessonId());
//                    lessonDTO.setTitle(lesson.getTitle());
//                    lessonDTO.setSortOrder(lesson.getSortOrder());
//                    lessonDTO.setContentType(lesson.getContentType());
//                    lessonDTO.setActive(true);
//                    lessonDTO.setCurrent(currentLesson != null
//                            && Objects.equals(currentLesson.getLessonId(), lesson.getLessonId()));
//
//                    lessonItems.add(lessonDTO);
//                }
//            }
//
//            chapterDTO.setLessons(lessonItems);
//            result.add(chapterDTO);
//        }
//
//        return result;
//    }
//
//    private LessonContentDTO toLessonContentDTO(Lesson lesson) {
//        LessonContentDTO dto = new LessonContentDTO();
//        dto.setLessonId(lesson.getLessonId());
//        dto.setTitle(lesson.getTitle());
//        dto.setContentType(lesson.getContentType());
//        dto.setVideoUrl(lesson.getVideoUrl());
//        dto.setPdfUrl(lesson.getPdfUrl());
//        dto.setContent(lesson.getContent());
//
//        if (lesson.getChapter() != null) {
//            dto.setChapterId(lesson.getChapter().getChapterId());
//            dto.setChapterTitle(lesson.getChapter().getTitle());
//        }
//
//        return dto;
//    }
//
//    private int indexOfLesson(List<Lesson> lessons, Integer lessonId) {
//        for (int i = 0; i < lessons.size(); i++) {
//            if (Objects.equals(lessons.get(i).getLessonId(), lessonId)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//}