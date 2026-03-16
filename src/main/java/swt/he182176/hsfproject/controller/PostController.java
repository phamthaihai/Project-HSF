package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.CommentForm;
import swt.he182176.hsfproject.dto.PostDTO;
import swt.he182176.hsfproject.entity.Post;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.PostCommentService;
import swt.he182176.hsfproject.service.PostService;

@Controller
public class PostController {

    private final PostService postService;
    private final PostCommentService postCommentService;

    public PostController(PostService postService, PostCommentService postCommentService) {
        this.postService = postService;
        this.postCommentService = postCommentService;
    }

    @GetMapping("/posts")
    public String showPostList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) String author,
                               Model model,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/";
        }

        model.addAttribute("posts", postService.searchPosts(keyword, category, author));
        model.addAttribute("categories", postService.getAllCategories());
        model.addAttribute("authors", postService.getAllAuthors());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("author", author);
        return "post-list";
    }

    @GetMapping("/posts/new")
    public String showNewForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/";
        }

        PostDTO dto = new PostDTO();
        dto.setStatus("Draft");

        model.addAttribute("postDTO", dto);
        model.addAttribute("isEdit", false);
        model.addAttribute("isMarketing", isMarketing(user));
        return "post-details";
    }

    @GetMapping("/posts/{id}")

    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/";
        }

        Post post = postService.getById(id);

        PostDTO dto = new PostDTO();
        dto.setPostId(post.getPostId());
        dto.setName(post.getName());
        dto.setType(post.getType());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());
        dto.setStatus(post.getStatus());

        model.addAttribute("postDTO", dto);
        model.addAttribute("post", post);
        model.addAttribute("isEdit", true);
        model.addAttribute("isMarketing", isMarketing(user));
        return "post-details";
    }

    @PostMapping("/posts/save")
    public String savePost(@Valid @ModelAttribute("postDTO") PostDTO postDTO,
                           BindingResult result,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           HttpSession session,
                           Model model) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", postDTO.getPostId() != null);
            model.addAttribute("isMarketing", isMarketing(user));
            return "post-details";
        }

        try {
            if (isMarketing(user) && postDTO.getPostId() != null) {
                Post old = postService.getById(postDTO.getPostId());
                postDTO.setStatus(old.getStatus());
            }

            if (isMarketing(user) && postDTO.getPostId() == null) {
                postDTO.setStatus("Draft");
            }

            postService.save(postDTO, user, imageFile);
            return "redirect:/posts";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isEdit", postDTO.getPostId() != null);
            model.addAttribute("isMarketing", isMarketing(user));
            return "post-details";
        }
    }

    @GetMapping("/posts/{id}/show")
    public String showPost(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user) || isMarketing(user)) {
            return "redirect:/posts";
        }

        postService.updateStatus(id, "Published");
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/hide")
    public String hidePost(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user) || isMarketing(user)) {
            return "redirect:/posts";
        }

        postService.updateStatus(id, "Hidden");
        return "redirect:/posts";
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
    public String showBlogDetails(@PathVariable Integer id, Model model, HttpSession session) {
        Post post = postService.getPublishedBlogById(id);
        User user = (User) session.getAttribute("user");

        model.addAttribute("blog", post);
        model.addAttribute("relatedBlogs", postService.getRelatedPublishedBlogs(post.getPostId(), post.getType()));
        model.addAttribute("comments", postCommentService.getCommentViewsByPost(post.getPostId()));
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("currentUser", user);

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