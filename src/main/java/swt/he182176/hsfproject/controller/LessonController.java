package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import swt.he182176.hsfproject.entity.Chapter;
import swt.he182176.hsfproject.entity.Lesson;
import swt.he182176.hsfproject.repository.ChapterRepository;
import swt.he182176.hsfproject.service.ContentService;

@Controller
public class LessonController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping("/courses/chapter/{chapterId}/lesson/add")
    public String addLesson(@PathVariable Integer chapterId, Model model) {
        Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
        if (chapter == null) {
            return "redirect:/courses";
        }
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        model.addAttribute("lesson", lesson);
        model.addAttribute("chapter", chapter);
        return "lesson-detail";
    }

    @GetMapping("/courses/lesson/edit/{id}")
    public String editLesson(@PathVariable Integer id, Model model) {
        Lesson lesson = contentService.getLessonById(id);
        if (lesson == null) {
            return "redirect:/courses";
        }
        model.addAttribute("lesson", lesson);
        model.addAttribute("chapter", lesson.getChapter());
        return "lesson-detail";
    }

    @PostMapping("/courses/lesson/save")
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson,
                             @RequestParam("chapterId") Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
        if (chapter == null) {
            return "redirect:/courses";
        }
        lesson.setChapter(chapter);
        contentService.saveLesson(lesson);
        Integer courseId = chapter.getCourse() != null ? chapter.getCourse().getCourseId() : null;
        if (courseId == null) {
            return "redirect:/courses";
        }
        return "redirect:/courses/" + courseId + "/content";
    }

    @GetMapping("/courses/chapter/{chapterId}/lesson/delete/{id}")
    public String deleteLesson(@PathVariable Integer chapterId, @PathVariable Integer id) {
        Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
        if (chapter == null) {
            return "redirect:/courses";
        }
        contentService.deleteLesson(id);
        Integer courseId = chapter.getCourse() != null ? chapter.getCourse().getCourseId() : null;
        if (courseId == null) {
            return "redirect:/courses";
        }
        return "redirect:/courses/" + courseId + "/content";
    }
}