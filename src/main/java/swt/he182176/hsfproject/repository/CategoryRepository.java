package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swt.he182176.hsfproject.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStatusIgnoreCase(String status);
}