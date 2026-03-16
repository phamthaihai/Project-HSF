package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.entity.Category;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.CategoryRepository;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.service.CourseAdminService;
import swt.he182176.hsfproject.service.ContentService;


import java.time.LocalDateTime;

@Controller
@RequestMapping("/courses")
public class CourseAdminController {

    @Autowired
    CourseAdminService courseService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContentService contentService;


    @GetMapping
    public String showCourseList(Model model){

        model.addAttribute("courses", courseService.getAllCourses());

        return "course-list";
    }


    @GetMapping("/add")
    public String showAddCourse(Model model){

        Course course = new Course();
        course.setInstructor(new User());
        course.setCategory(new Category());

        model.addAttribute("course", course);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("managers", userRepository.findByRole_Name("MANAGER"));
        course.setCreateAt(LocalDateTime.now());

        return "course-detail";
    }



    @GetMapping("/edit/{id}")
    public String showEditCourse(@PathVariable int id, Model model){
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("managers", userRepository.findByRole_Name("MANAGER"));
        return "course-detail";
    }

    @GetMapping("/{id}/content")
    public String showCourseContent(@PathVariable int id, Model model){
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        model.addAttribute("chapters", contentService.getChaptersByCourse(id));
        return "course-content";
    }


    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course){
        if (course.getCourseId() == null) {
            course.setCreateAt(LocalDateTime.now());
        } else {
            Course existing = courseService.getCourseById(course.getCourseId());
            if (existing != null) {
                course.setCreateAt(existing.getCreateAt());
            }
        }
        courseService.updateCourse(course);
        return "redirect:/courses";
    }


    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id){

        courseService.deleteCourse(id);

        return "redirect:/courses";
    }

}