package swt.he182176.hsfproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import swt.he182176.hsfproject.entity.Setting;
import swt.he182176.hsfproject.repository.SettingRepository;

@Configuration
public class DataSetting {

    @Bean
    CommandLineRunner initSettingTypes(SettingRepository settingRepository) {
        return args -> {
            Setting userRole = seedRootType(settingRepository, "User Role", 1);
            Setting postCategory = seedRootType(settingRepository, "Post Category", 2);
            Setting courseCategory = seedRootType(settingRepository, "Course Category", 3);

            // Seed User Role children
            if (userRole != null) {
                seedChildType(settingRepository, "Admin", userRole, 1);
                seedChildType(settingRepository, "Manager", userRole, 2);
                seedChildType(settingRepository, "Teacher", userRole, 3);
            }
        };
    }

    private Setting seedRootType(SettingRepository repo, String name, int priority) {
        return repo.findByNameIgnoreCaseAndTypeIsNull(name).orElseGet(() -> {
            Setting setting = new Setting();
            setting.setName(name);
            setting.setType(null);
            setting.setValue(null);
            setting.setPriority(priority);
            setting.setStatus(true);
            setting.setDescription(name + " root type");
            return repo.save(setting);
        });
    }

    private void seedChildType(SettingRepository repo, String name, Setting parent, int priority) {
        if (!repo.existsDuplicateName(name, parent.getId(), null)) {
            Setting setting = new Setting();
            setting.setName(name);
            setting.setType(parent);
            setting.setValue(null);
            setting.setPriority(priority);
            setting.setStatus(true);
            setting.setDescription(name);
            repo.save(setting);
        }
    }

}