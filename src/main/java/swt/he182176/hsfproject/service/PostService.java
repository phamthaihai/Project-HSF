package swt.he182176.hsfproject.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swt.he182176.hsfproject.dto.PostDTO;
import swt.he182176.hsfproject.entity.Post;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    public PostService(PostRepository postRepository,
                       FileUploadService fileUploadService) {
        this.postRepository = postRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<Post> searchPosts(String keyword, String category, String author) {
        return postRepository.searchPosts(normalize(keyword), normalize(category), normalize(author));
    }

    public List<Post> searchPublishedBlogs(String keyword, String category) {
        return postRepository.searchPublishedBlogs(normalize(keyword), normalize(category));
    }

    public List<String> getAllCategories() {
        return postRepository.findAllCategories();
    }

    public List<String> getAllAuthors() {
        return postRepository.findAllAuthors();
    }

    public Post getById(Integer id) {
        return postRepository.findByPostId(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post getPublishedBlogById(Integer id) {
        return postRepository.findPublishedById(id)
                .orElseThrow(() -> new RuntimeException("Published post not found"));
    }

    public List<Post> getLatestPublishedPosts() {
        return postRepository.findPublishedPosts(PageRequest.of(0, 4));
    }

    public List<Post> getLatestPosts() {
        try {
            return postRepository.findPublishedPosts(PageRequest.of(0, 4));
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Post> getRelatedPublishedBlogs(Integer currentId, String category) {
        if (category == null || category.isBlank()) return List.of();
        return postRepository.findRelatedPublishedBlogs(currentId, category);
    }

    @Transactional

    public Post save(PostDTO dto, User currentUser, MultipartFile imageFile) {
        Post post;

        if (dto.getPostId() != null) {
            post = getById(dto.getPostId());
        } else {
            post = new Post();
            post.setUser(currentUser);
        }

        post.setName(dto.getName());
        post.setType(dto.getType());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());
        post.setStatus(dto.getStatus());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.uploadPostImage(imageFile);
            post.setThumbnailUrl(imageUrl);
        }

        return postRepository.save(post);
    }

    @Transactional
    public void updateStatus(Integer postId, String status) {
        Post post = getById(postId);
        post.setStatus(status);
        postRepository.save(post);
    }

    private String normalize(String s) {
        return (s == null || s.trim().isBlank()) ? null : s.trim();
    }



}
