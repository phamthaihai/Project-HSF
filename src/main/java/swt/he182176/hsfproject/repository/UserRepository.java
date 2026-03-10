package swt.he182176.hsfproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swt.he182176.hsfproject.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    Optional<User> findByVerifyToken(String verifyToken);

    List<User> findByRole_Name(String roleName);

    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(String email, String fullName);
}