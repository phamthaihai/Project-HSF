package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.entity.Chapter;
import swt.he182176.hsfproject.entity.Lesson;

import java.util.List;

public interface ContentService {
    List<Chapter> getChaptersByCourse(Integer courseId);
    Chapter getChapterById(Integer id);
    void deleteChapter(Integer id);
    void saveChapter(Chapter chapter);


    List<Lesson> getLessonsByChapter(Integer chapterId);
    Lesson getLessonById(Integer id);
    void deleteLesson(Integer id);
    void saveLesson(Lesson lesson);
}
