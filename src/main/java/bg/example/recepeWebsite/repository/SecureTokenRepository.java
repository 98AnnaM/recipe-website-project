package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.entity.SecureTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureTokenEntity, Long> {
    Optional<SecureTokenEntity> findByToken(String token);
}
