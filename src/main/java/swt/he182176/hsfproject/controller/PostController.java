package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.CommentForm;
import swt.he182176.hsfproject.dto.PostDTO;
import swt.he182176.hsfproject.entity.Post;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.PostCommentService;
import swt.he182176.hsfproject.service.PostService;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostCommentService postCommentService;

    @GetMapping("/posts")
    public String showPostList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) String status,
                               Model model,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManagePosts(user)) {
            return "redirect:/homepage";
        }

        model.addAttribute("posts", postService.searchPosts(keyword, category, status));
        model.addAttribute("categories", postService.getPublishedCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        return "post-list";
    }

    @GetMapping("/posts/new")
    public String showNewPostForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");


        PostDTO dto = new PostDTO();
        dto.setStatus("Draft");

        model.addAttribute("postDTO", dto);
        model.addAttribute("categories", postService.getPublishedCategories());
        model.addAttribute("isEdit", false);
        return "post-details";
    }

    @GetMapping("/posts/{id}")
    public String showEditPostForm(@PathVariable Integer id,
                                   Model model,
                                   HttpSession session) {
        User user = (User) session.getAttribute("user");


        Post post = postService.getById(id);

        if (isMarketing(user) && post.getUser() != null && !post.getUser().getId().equals(user.getId())) {
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
        model.addAttribute("categories", postService.getPublishedCategories());
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


        if (result.hasErrors()) {
            model.addAttribute("categories", postService.getPublishedCategories());
            model.addAttribute("isEdit", postDTO.getPostId() != null);
            return "post-details";
        }

        try {
            Post existing = null;
            if (postDTO.getPostId() != null) {
                existing = postService.getById(postDTO.getPostId());

                if (isMarketing(user) && existing.getUser() != null && !existing.getUser().getId().equals(user.getId())) {
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
            model.addAttribute("categories", postService.getPublishedCategories());
            model.addAttribute("isEdit", postDTO.getPostId() != null);
            return "post-details";
        }
    }

    @GetMapping("/blogs")
    public String showBlogList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String category,
                               Model model) {
        model.addAttribute("blogs", postService.searchPublishedBlogs(keyword, category));
        model.addAttribute("categories", postService.getPublishedCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "blog-list";
    }

    @GetMapping("/blogs/{id}")
    public String showBlogDetails(@PathVariable Integer id,
                                  Model model,
                                  HttpSession session) {
        Post post = postService.getPublishedBlogById(id);
        User user = (User) session.getAttribute("user");

        model.addAttribute("blog", post);
        model.addAttribute("relatedBlogs", postService.getRelatedPublishedBlogs(post.getPostId(), post.getType()));
        model.addAttribute("comments", postCommentService.getCommentViewsByPost(post.getPostId()));
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("currentUser", user);

        return "blog-details";
    }

    @PostMapping("/blogs/{id}/comments")
    public String addComment(@PathVariable Integer id,
                             @Valid @ModelAttribute("commentForm") CommentForm commentForm,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        Post post = postService.getPublishedBlogById(id);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "Comment content is required");
            return "redirect:/blogs/" + id;
        }

        try {
            postCommentService.addComment(post, user, commentForm);
            redirectAttributes.addFlashAttribute("commentSuccess", "Comment added successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("commentError", e.getMessage());
        }

        return "redirect:/blogs/" + id;
    }

    @PostMapping("/blogs/{blogId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Integer blogId,
                                @PathVariable Integer commentId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        try {
            postCommentService.deleteComment(commentId, user);
            redirectAttributes.addFlashAttribute("commentSuccess", "Comment deleted successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("commentError", e.getMessage());
        }

        return "redirect:/blogs/" + blogId;
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