package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.entity.PictureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    Page<PictureEntity> findAllByAuthor_Username(String username, Pageable pageable);

    void deleteAllById(Long id);


}
