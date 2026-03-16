package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swt.he182176.hsfproject.entity.PostComment;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    List<PostComment> findByPost_PostIdOrderByCreatedAtAscIdAsc(Integer postId);

    List<PostComment> findByParent_IdOrderByCreatedAtAscIdAsc(Integer parentId);
}