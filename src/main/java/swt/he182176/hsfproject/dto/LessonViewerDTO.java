package swt.he182176.hsfproject.dto;

import java.util.ArrayList;
import java.util.List;

public class LessonViewerDTO {

    private Integer courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseThumbnailUrl;
    private List<ChapterItemDTO> chapters = new ArrayList<>();
    private LessonContentDTO currentLesson;
    private Integer previousLessonId;
    private Integer nextLessonId;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseThumbnailUrl() {
        return courseThumbnailUrl;
    }

    public void setCourseThumbnailUrl(String courseThumbnailUrl) {
        this.courseThumbnailUrl = courseThumbnailUrl;
    }

    public List<ChapterItemDTO> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterItemDTO> chapters) {
        this.chapters = chapters;
    }

    public LessonContentDTO getCurrentLesson() {
        return currentLesson;
    }

    public void setCurrentLesson(LessonContentDTO currentLesson) {
        this.currentLesson = currentLesson;
    }

    public Integer getPreviousLessonId() {
        return previousLessonId;
    }

    public void setPreviousLessonId(Integer previousLessonId) {
        this.previousLessonId = previousLessonId;
    }

    public Integer getNextLessonId() {
        return nextLessonId;
    }

    public void setNextLessonId(Integer nextLessonId) {
        this.nextLessonId = nextLessonId;
    }
}