package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findByChapter_ChapterIdAndStatusIgnoreCaseOrderBySortOrderAsc(Integer chapterId, String status);

    @Query("""
        select l
        from Lesson l
        join fetch l.chapter ch
        join fetch ch.course c
        where c.courseId = :courseId
          and lower(coalesce(ch.status, 'ACTIVE')) = lower(:status)
          and lower(coalesce(l.status, 'ACTIVE')) = lower(:status)
        order by coalesce(ch.sortOrder, 0), coalesce(l.sortOrder, 0)
    """)
    List<Lesson> findActiveLessonsByCourseId(@Param("courseId") Integer courseId,
                                             @Param("status") String status);
}