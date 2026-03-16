package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swt.he182176.hsfproject.entity.Chapter;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    List<Chapter> findByCourse_CourseIdAndStatusIgnoreCaseOrderBySortOrderAsc(Integer courseId, String status);
}