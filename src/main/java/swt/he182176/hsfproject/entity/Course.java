package swt.he182176.hsfproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer courseId;

    @Column(name="title", columnDefinition = "NVARCHAR(500)")
    private String title;

    @Column(name="description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name="price")
    private double price;

    @Column(name="sale_price")
    private Double salePrice;

    @Column(name="level", columnDefinition = "NVARCHAR(50)")
    private String level;

    @Column(name="duration")
    private Integer duration;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="instructor_id")
    private User instructor;

    @Column(name="thumbnail_url", columnDefinition = "NVARCHAR(1000)")
    private String thumbnailUrl;

    @Column(name="published", nullable = false)
    private Boolean published;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("chapterOrder ASC")
    private List<Chapter> chapters = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createAt;

    public Course() {}

    public Course(Integer courseId, LocalDateTime createAt, Boolean published, String thumbnailUrl
            , Category category, User instructor, Integer duration, double price
            , String level, String description, String title) {
        this.courseId = courseId;
        this.createAt = createAt;
        this.published = published;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.instructor = instructor;
        this.duration = duration;
        this.price = price;
        this.level = level;
        this.description = description;
        this.title = title;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public Boolean isPublished() {
        return published;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
