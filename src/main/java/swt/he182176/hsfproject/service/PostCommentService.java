package swt.he182176.hsfproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swt.he182176.hsfproject.dto.CommentForm;
import swt.he182176.hsfproject.dto.CommentView;
import swt.he182176.hsfproject.entity.Post;
import swt.he182176.hsfproject.entity.PostComment;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.PostCommentRepository;

import java.util.*;

@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public PostCommentService(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    public List<CommentView> getCommentViewsByPost(Integer postId) {
        List<PostComment> all = postCommentRepository.findByPost_PostIdOrderByCreatedAtAscIdAsc(postId);

        Map<Integer, List<PostComment>> childrenMap = new LinkedHashMap<>();
        List<PostComment> roots = new ArrayList<>();

        for (PostComment c : all) {
            if (c.getParent() == null) {
                roots.add(c);
            } else {
                Integer parentId = c.getParent().getId();
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(c);
            }
        }

        List<CommentView> result = new ArrayList<>();
        for (PostComment root : roots) {
            appendComment(result, root, childrenMap, 0);
        }
        return result;
    }

    private void appendComment(List<CommentView> result,
                               PostComment comment,
                               Map<Integer, List<PostComment>> childrenMap,
                               int depth) {

        result.add(new CommentView(
                comment.getId(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getPost().getPostId(),
                comment.getUser().getId(),
                comment.getUser().getFullName(),
                comment.getContent(),
                depth,
                comment.getCreatedAt()
        ));

        List<PostComment> children = childrenMap.get(comment.getId());
        if (children != null) {
            for (PostComment child : children) {
                appendComment(result, child, childrenMap, depth + 1);
            }
        }
    }

    @Transactional
    public void addComment(Post post, User user, CommentForm form) {
        if (user == null) {
            throw new RuntimeException("You must login first to comment");
        }

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(form.getContent().trim());

        if (form.getParentId() != null) {
            PostComment parent = postCommentRepository.findById(form.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            if (!Objects.equals(parent.getPost().getPostId(), post.getPostId())) {
                throw new RuntimeException("Invalid parent comment");
            }

            comment.setParent(parent);
        }

        postCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer commentId, User currentUser) {
        if (currentUser == null) {
            throw new RuntimeException("You must login first");
        }

        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!canDelete(comment, currentUser)) {
            throw new RuntimeException("You do not have permission to delete this comment");
        }

        deleteRecursive(comment);
    }

    private boolean canDelete(PostComment comment, User currentUser) {
        if (currentUser.getId().equals(comment.getUser().getId())) {
            return true;
        }

        if (currentUser.getRole() == null || currentUser.getRole().getName() == null) {
            return false;
        }

        String role = currentUser.getRole().getName().toUpperCase();
        return role.equals("ADMIN") || role.equals("MANAGER") || role.equals("MARKETING");
    }

    private void deleteRecursive(PostComment comment) {
        List<PostComment> children = postCommentRepository.findByParent_IdOrderByCreatedAtAscIdAsc(comment.getId());
        for (PostComment child : children) {
            deleteRecursive(child);
        }
        postCommentRepository.delete(comment);
    }
}