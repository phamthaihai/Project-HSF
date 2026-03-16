package swt.he182176.hsfproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.entity.Chapter;
import swt.he182176.hsfproject.entity.Lesson;
import swt.he182176.hsfproject.repository.ChapterRepository;
import swt.he182176.hsfproject.repository.LessonRepository;
import swt.he182176.hsfproject.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ChapterRepository chapterRepo;

    @Autowired
    private LessonRepository lessonRepo;

    @Override
    public java.util.List<Chapter> getChaptersByCourse(Integer courseId) {
        java.util.List<Chapter> chapters = chapterRepo.findByCourse_CourseIdOrderByChapterOrderAsc(courseId);
        for (Chapter ch : chapters) {
            ch.setLessons(lessonRepo.findByChapter_ChapterIdOrderByOrderIndexAsc(ch.getChapterId()));
        }
        return chapters;
    }

    @Override public Chapter getChapterById(Integer id) {
        return chapterRepo.findById(id).orElse(null);
    }

    @Override public void saveChapter(Chapter chapter) {
        chapterRepo.save(chapter);
    }

    @Override public void deleteChapter(Integer id) {
        chapterRepo.deleteById(id);
    }

    @Override
    public java.util.List<Lesson> getLessonsByChapter(Integer chapterId) {
        return lessonRepo.findByChapter_ChapterIdOrderByOrderIndexAsc(chapterId);
    }

    @Override public Lesson getLessonById(Integer id) {
        return lessonRepo.findById(id).orElse(null);
    }

    @Override public void saveLesson(Lesson lesson) {
        lessonRepo.save(lesson);
    }

    @Override public void deleteLesson(Integer id) {
        lessonRepo.deleteById(id);
    }


}
