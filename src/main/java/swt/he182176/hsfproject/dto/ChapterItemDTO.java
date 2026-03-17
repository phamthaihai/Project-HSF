package swt.he182176.hsfproject.dto;

import java.util.ArrayList;
import java.util.List;

public class ChapterItemDTO {

    private Integer chapterId;
    private String title;
    private Integer sortOrder;
    private List<LessonItemDTO> lessons = new ArrayList<>();

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<LessonItemDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonItemDTO> lessons) {
        this.lessons = lessons;
    }
}