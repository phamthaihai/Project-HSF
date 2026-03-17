package swt.he182176.hsfproject.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chapterId;

    @Column(name="name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name="title", columnDefinition = "NVARCHAR(500)")
    private String title;

    @Column(name="description", columnDefinition = "NVARCHAR(MAX)")
    private String chapterDescription;

    @Column(name = "[order]")
    private Integer chapterOrder;

    @ManyToOne()
    @JoinColumn(name="course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Lesson> lessons = new ArrayList<>();

    @Column(name="thumbnail_url", columnDefinition = "NVARCHAR(1000)")
    private String thumbnailUrl;

    public Chapter(){}

    public Chapter(Integer chapterId, String name, String title, String chapterDescription, Integer chapterOrder, Course course, Lesson lesson, String thumbnailUrl) {
        this.chapterId = chapterId;
        this.name = name;
        this.title = title;
        this.chapterDescription = chapterDescription;
        this.chapterOrder = chapterOrder;
        this.course = course;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return chapterDescription;
    }

    public void setDescription(String chapterDescription) {
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

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}