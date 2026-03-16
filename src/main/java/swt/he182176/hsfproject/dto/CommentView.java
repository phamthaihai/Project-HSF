package swt.he182176.hsfproject.dto;

import java.time.LocalDateTime;

public class CommentView {

    private Integer id;
    private Integer parentId;
    private Integer postId;
    private Integer userId;
    private String userFullName;
    private String content;
    private int depth;
    private LocalDateTime createdAt;

    public CommentView() {
    }

    public CommentView(Integer id, Integer parentId, Integer postId, Integer userId,
                       String userFullName, String content, int depth, LocalDateTime createdAt) {
        this.id = id;
        this.parentId = parentId;
        this.postId = postId;
        this.userId = userId;
        this.userFullName = userFullName;
        this.content = content;
        this.depth = depth;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getContent() {
        return content;
    }

    public int getDepth() {
        return depth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}