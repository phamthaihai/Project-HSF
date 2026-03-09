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
            seedRootType(settingRepository, "User Role", 1);
            seedRootType(settingRepository, "Post Category", 2);
            seedRootType(settingRepository, "Course Category", 3);
        };
    }

    private void seedRootType(SettingRepository repo, String name, int priority) {
        if (repo.findByNameIgnoreCaseAndTypeIsNull(name).isEmpty()) {
            Setting setting = new Setting();
            setting.setName(name);
            setting.setType(null);
            setting.setValue(null);
            setting.setPriority(priority);
            setting.setStatus(true);
            setting.setDescription(name + " root type");
            repo.save(setting);
        }
    }
}