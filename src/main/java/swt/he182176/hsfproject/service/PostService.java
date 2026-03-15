package swt.he182176.hsfproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swt.he182176.hsfproject.dto.PostDTO;
import swt.he182176.hsfproject.entity.Post;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> searchPosts(String keyword, String category, String status) {
        return postRepository.searchPosts(normalize(keyword), normalize(category), normalize(status));
    }

    public List<Post> searchPublishedBlogs(String keyword, String category) {
        return postRepository.searchPublishedBlogs(normalize(keyword), normalize(category));
    }

    public List<String> getPublishedCategories() {
        return postRepository.findPublishedCategories();
    }

    public List<Post> getRelatedPublishedBlogs(Integer currentId, String category) {
        if (category == null || category.trim().isBlank()) {
            return List.of();
        }
        return postRepository.findRelatedPublishedBlogs(currentId, category.trim());
    }

    public Post getById(Integer id) {
        return postRepository.findByPostId(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post getPublishedBlogById(Integer id) {
        return postRepository.findPublishedById(id)
                .orElseThrow(() -> new RuntimeException("Published blog not found"));
    }

    @Transactional
    public Post save(PostDTO dto, User currentUser) {
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

        return postRepository.save(post);
    }

    private String normalize(String value) {
        if (value == null || value.trim().isBlank()) {
            return null;
        }
        return value.trim();
    }
}