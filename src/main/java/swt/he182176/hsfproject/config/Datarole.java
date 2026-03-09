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
            seed(roleRepository, "ADMIN");
            seed(roleRepository, "MANAGER");
            seed(roleRepository, "MEMBER");
            seed(roleRepository, "MARKETING"); // theo spec có marketing

            if (roleRepository.findByName("MARKETING") == null) {
                Role member = new Role();
                member.setName("MARKETING");
                roleRepository.save(member);
            }

            System.out.println("Roles update successfully!");
        };
    }


    private void seed(RoleRepository repo, String name) {
        if (repo.findByName(name).isEmpty()) {
            Role r = new Role();
            r.setName(name);
            r.setStatus(true);
            repo.save(r);
        }
    }
}
