package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>,
        JpaSpecificationExecutor<RecipeEntity> {

    Page<RecipeEntity> findAllByCategory(CategoryNameEnum categoryName, Pageable pageable);

    @Query("SELECT r FROM RecipeEntity r " +
            "JOIN r.comments c " +
            "WHERE size(r.comments) = (SELECT max(size(r2.comments)) FROM RecipeEntity r2)" +
            "ORDER BY r.id ASC")
    List<RecipeEntity> findRecipeWithMostComments();

    Page<RecipeEntity> findAllByAuthor_Id(Long authorId, Pageable pageable);

    @Query("SELECT r FROM RecipeEntity r JOIN r.favoriteUsers u WHERE u.id = :userId")
    Page<RecipeEntity> findAllFavoriteRecipes(@Param("userId") Long userId, Pageable pageable);
}

