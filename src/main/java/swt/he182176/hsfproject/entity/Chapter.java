package swt.he182176.hsfproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name="chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chapterId;

    @Column(name="name")
    private String Name;

    @Column(name="title")
    private String chaptertTitle;

    @Column(name="description")
    private String chapterDescription;

    @Column(name = "order")
    private Integer chapterOrder;

    @ManyToOne()
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToOne()
    @JoinColumn(name="lesson_id")
    private Lesson lesson;

    @Column(name="thumbnail_url")
    private String thumbnailUrl;

    public Chapter(){}

    public Chapter(Integer chapterId, String name, String chaptertTitle, String chapterDescription, Integer chapterOrder, Course course, Lesson lesson, String thumbnailUrl) {
        this.chapterId = chapterId;
        Name = name;
        this.chaptertTitle = chaptertTitle;
        this.chapterDescription = chapterDescription;
        this.chapterOrder = chapterOrder;
        this.course = course;
        this.lesson = lesson;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getChaptertTitle() {
        return chaptertTitle;
    }

    public void setChaptertTitle(String chaptertTitle) {
        this.chaptertTitle = chaptertTitle;
    }

    public String getChapterDescription() {
        return chapterDescription;
    }

    public void setChapterDescription(String chapterDescription) {
        this.chapterDescription = chapterDescription;
    }

    public Integer getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(Integer chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
