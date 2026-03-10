package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swt.he182176.hsfproject.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
