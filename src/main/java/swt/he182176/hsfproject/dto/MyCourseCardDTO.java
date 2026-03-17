package swt.he182176.hsfproject.dto;

public class MyCourseCardDTO {

    private Integer courseId;
    private String title;
    private String description;
    private String level;
    private Integer duration;
    private String thumbnailUrl;
    private String categoryName;
    private String instructorName;
    private String startLearningUrl;
    
    private java.time.LocalDateTime enrolledDate;
    private Double progressPercent;
    private String progressStatus;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getStartLearningUrl() {
        return startLearningUrl;
    }

    public void setStartLearningUrl(String startLearningUrl) {
        this.startLearningUrl = startLearningUrl;
    }

    public java.time.LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(java.time.LocalDateTime enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public Double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }
}