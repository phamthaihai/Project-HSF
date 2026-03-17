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
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.repository.CourseRepository;
import swt.he182176.hsfproject.service.ContentService;

@Controller
public class ChapterController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses/chapter/edit/{id}")
    public String editChapter(Model model, @PathVariable Integer id) {
        Chapter chapter = contentService.getChapterById(id);
        if (chapter == null) {
            return "redirect:/courses";
        }
        model.addAttribute("chapter", chapter);
        model.addAttribute("course", chapter.getCourse());
        return "chapter-detail";
    }

    @GetMapping("/courses/{courseId}/chapter/add")
    public String addChapter(Model model, @PathVariable Integer courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return "redirect:/courses";
        }
        Chapter chapter = new Chapter();
        chapter.setCourse(course);
        model.addAttribute("chapter", chapter);
        model.addAttribute("course", course);
        return "chapter-detail";
    }

    @PostMapping("/courses/chapter/save")
    public String saveChapter(@ModelAttribute("chapter") Chapter chapter,
                              @RequestParam("courseId") Integer courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return "redirect:/courses";
        }
        chapter.setCourse(course);
        contentService.saveChapter(chapter);
        return "redirect:/courses/" + courseId + "/content";
    }

    @GetMapping("/courses/{courseId}/chapter/delete/{id}")
    public String deleteChapter(@PathVariable Integer courseId, @PathVariable Integer id) {
        contentService.deleteChapter(id);
        return "redirect:/courses/" + courseId + "/content";
    }
}