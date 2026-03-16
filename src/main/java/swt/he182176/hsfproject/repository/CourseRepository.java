package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
        select c
        from Course c
        where c.published = true
          and (
                :keyword is null
                or trim(:keyword) = ''
                or lower(c.title) like lower(concat('%', :keyword, '%'))
              )
          and (
                :categoryId is null
                or c.category.categoryId = :categoryId
              )
        order by lower(c.title) asc
    """)
    List<Course> findPublicCourses(@Param("keyword") String keyword,
                                   @Param("categoryId") Integer categoryId);
}