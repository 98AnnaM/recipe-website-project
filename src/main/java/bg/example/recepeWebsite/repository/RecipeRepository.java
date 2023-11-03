package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT r FROM RecipeEntity r JOIN r.types c WHERE c.name = :categoryName")
    List<RecipeEntity> findAllByCategoryName(CategoryNameEnum categoryName);

    @Query("SELECT r FROM RecipeEntity r " +
            "JOIN r.comments c " +
            "WHERE size(r.comments) = (SELECT max(size(r2.comments)) FROM RecipeEntity r2)" +
            "ORDER BY r.id ASC")
    List<RecipeEntity> findRecipeWithMostComments();
}

