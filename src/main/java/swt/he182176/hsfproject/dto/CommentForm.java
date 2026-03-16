package swt.he182176.hsfproject.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentForm {

    @NotBlank(message = "Comment content is required")
    private String content;

    private Integer parentId;

    public CommentForm() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}