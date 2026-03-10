package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.dto.PostDTO;
import swt.he182176.hsfproject.entity.Post;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.PostService;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public String showPostList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) String status,
                               Model model,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/login";
        }

        model.addAttribute("posts", postService.searchPosts(keyword, category, status));
        model.addAttribute("categories", postService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        return "post-list";
    }

    @GetMapping("/posts/new")
    public String showNewPostForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/login";
        }

        PostDTO dto = new PostDTO();
        dto.setStatus("Draft");

        model.addAttribute("postDTO", dto);
        model.addAttribute("categories", postService.getAllCategories());
        model.addAttribute("isEdit", false);
        return "post-details";
    }

    @GetMapping("/posts/{id}")
    public String showEditPostForm(@PathVariable Integer id,
                                   Model model,
                                   HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/login";
        }

        Post post = postService.getById(id);

        if (isMarketing(user) && post.getUser() != null && post.getUser().getId() != user.getId()) {
            return "redirect:/posts";
        }

        PostDTO dto = new PostDTO();
        dto.setPostId(post.getPostId());
        dto.setName(post.getName());
        dto.setType(post.getType());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());
        dto.setStatus(post.getStatus());

        model.addAttribute("postDTO", dto);
        model.addAttribute("categories", postService.getAllCategories());
        model.addAttribute("isEdit", true);
        model.addAttribute("post", post);
        return "post-details";
    }

    @PostMapping("/posts/save")
    public String savePost(@Valid @ModelAttribute("postDTO") PostDTO postDTO,
                           BindingResult result,
                           Model model,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", postService.getAllCategories());
            model.addAttribute("isEdit", postDTO.getPostId() != null);
            return "post-details";
        }

        try {
            Post existing = null;
            if (postDTO.getPostId() != null) {
                existing = postService.getById(postDTO.getPostId());

                if (isMarketing(user) && existing.getUser() != null && existing.getUser().getId() != user.getId()) {
                    return "redirect:/posts";
                }
            }

            if (isMarketing(user) && existing != null) {
                postDTO.setStatus(existing.getStatus());
            }

            postService.save(postDTO, user);
            return "redirect:/posts";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", postService.getAllCategories());
            model.addAttribute("isEdit", postDTO.getPostId() != null);
            return "post-details";
        }
    }

    @GetMapping("/blogs")
    public String showBlogList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String category,
                               Model model) {
        model.addAttribute("blogs", postService.searchPublishedBlogs(keyword, category));
        model.addAttribute("categories", postService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "blog-list";
    }

    @GetMapping("/blogs/{id}")
    public String showBlogDetails(@PathVariable Integer id, Model model) {
        Post post = postService.getPublishedBlogById(id);
        model.addAttribute("blog", post);
        model.addAttribute("categories", postService.getAllCategories());
        model.addAttribute("relatedBlogs", postService.searchPublishedBlogs(null, post.getType()));
        return "blog-details";
    }

    private boolean canManagePosts(User user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }
        String role = user.getRole().getName().toUpperCase();
        return role.equals("ADMIN") || role.equals("MANAGER") || role.equals("MARKETING");
    }

    private boolean isMarketing(User user) {
        return user != null
                && user.getRole() != null
                && user.getRole().getName() != null
                && "MARKETING".equalsIgnoreCase(user.getRole().getName());
    }
}