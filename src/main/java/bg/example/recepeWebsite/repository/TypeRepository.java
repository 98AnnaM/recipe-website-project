package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {

    Optional<TypeEntity> findByName(TypeNameEnum name);
}
