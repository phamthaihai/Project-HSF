package swt.he182176.hsfproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name="lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonId;

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(500)")
    private String title;

    @Column(name = "content_type", columnDefinition = "NVARCHAR(50)")
    private String contentType;

    @Column(name = "content_video", columnDefinition = "NVARCHAR(1000)")
    private String contentVideo;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "preview_lesson")
    private Boolean isPreview = false;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public Lesson(Integer lessonId, String title, String contentType, String contentVideo, Integer duration, Integer orderIndex, Boolean isPreview, Chapter chapter) {
        this.lessonId = lessonId;
        this.title = title;
        this.contentType = contentType;
        this.contentVideo = contentVideo;
        this.duration = duration;
        this.orderIndex = orderIndex;
        this.isPreview = isPreview;
        this.chapter = chapter;
    }

    public Lesson(){}

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentVideo() {
        return contentVideo;
    }

    public void setContentVideo(String contentVideo) {
        this.contentVideo = contentVideo;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Boolean getPreview() {
        return isPreview;
    }

    public void setPreview(Boolean preview) {
        isPreview = preview;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
