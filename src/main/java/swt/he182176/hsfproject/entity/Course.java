package swt.he182176.hsfproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int courseId;

    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name="price")
    private double price;

    @Column(name="level")
    private String level;

    @Column(name="duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category categoryId;

    @ManyToOne
    @JoinColumn(name="instructor_id")
    private User instructor;

    @Column(name="thumbnail_url")
    private String thumbnailUrl;

    @Column(name="pulished")
    private boolean pulished;

    private LocalDateTime createAt;

    public Course() {}

    public Course(int courseId, LocalDateTime createAt, boolean pulished, String thumbnailUrl
            , Category categoryId, User instructor, int duration, double price
            , String level, String description, String title) {
        this.courseId = courseId;
        this.createAt = createAt;
        this.pulished = pulished;
        this.thumbnailUrl = thumbnailUrl;
        this.categoryId = categoryId;
        this.instructor = instructor;
        this.duration = duration;
        this.price = price;
        this.level = level;
        this.description = description;
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isPulished() {
        return pulished;
    }

    public void setPulished(boolean pulished) {
        this.pulished = pulished;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
