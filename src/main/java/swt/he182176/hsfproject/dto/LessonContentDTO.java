package swt.he182176.hsfproject.dto;

public class LessonContentDTO {

    private Integer lessonId;
    private String title;
    private String contentType;
    private String videoUrl;
    private String pdfUrl;
    private String content;
    private Integer chapterId;
    private String chapterTitle;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public boolean isVideoType() {
        return contentType != null && contentType.equalsIgnoreCase("VIDEO");
    }

    public boolean isPdfType() {
        return contentType != null && contentType.equalsIgnoreCase("PDF");
    }

    public boolean isRichTextType() {
        return contentType != null && contentType.equalsIgnoreCase("RICH_TEXT");
    }
}