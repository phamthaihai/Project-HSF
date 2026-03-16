package swt.he182176.hsfproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import swt.he182176.hsfproject.entity.Category;
import swt.he182176.hsfproject.repository.CategoryRepository;

@Configuration
public class CategoryDataInit {

    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            saveIfNotExists(categoryRepository, "Programming", "Lập trình và phát triển phần mềm", "ACTIVE");
            saveIfNotExists(categoryRepository, "Business", "Kinh doanh và quản lý", "ACTIVE");
            saveIfNotExists(categoryRepository, "Data Science", "Khoa học dữ liệu và phân tích", "ACTIVE");
            saveIfNotExists(categoryRepository, "Design", "Thiết kế đồ họa, UI/UX, và sáng tạo", "ACTIVE");
            saveIfNotExists(categoryRepository, "Marketing", "Digital Marketing, SEO, và quảng cáo", "ACTIVE");
            saveIfNotExists(categoryRepository, "Personal Development", "Phát triển cá nhân và kỹ năng mềm", "ACTIVE");
            saveIfNotExists(categoryRepository, "Health", "Sức khỏe, thể hình và dinh dưỡng", "ACTIVE");
            saveIfNotExists(categoryRepository, "Photography", "Nhiếp ảnh và quay phim", "ACTIVE");
            saveIfNotExists(categoryRepository, "Music", "Âm nhạc và nhạc cụ", "ACTIVE");
            saveIfNotExists(categoryRepository, "Academics", "Học thuật và giáo dục", "ACTIVE");
            saveIfNotExists(categoryRepository, "Language", "Ngoại ngữ và giao tiếp", "ACTIVE");
            saveIfNotExists(categoryRepository, "Lifestyle", "Phong cách sống và giải trí", "ACTIVE");
        };
    }

    private void saveIfNotExists(CategoryRepository repo, String name, String description, String status) {
        if (!repo.existsByName(name)) {
            Category c = new Category();
            c.setName(name);
            c.setDescription(description);
            c.setStatus(status);
            repo.save(c);
        }
    }
}
