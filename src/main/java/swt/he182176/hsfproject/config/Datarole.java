package swt.he182176.hsfproject.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.repository.RoleRepository;

@Configuration
public class Datarole {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.findByName("ADMIN") == null) {
                Role admin = new Role();
                admin.setName("ADMIN");
                roleRepository.save(admin);
            }

            if (roleRepository.findByName("MANAGER") == null) {
                Role manager = new Role();
                manager.setName("MANAGER");
                roleRepository.save(manager);
            }

            if (roleRepository.findByName("MEMBER") == null) {
                Role member = new Role();
                member.setName("MEMBER");
                roleRepository.save(member);
            }

            System.out.println("Roles update successfully!");
        };
    }
}
