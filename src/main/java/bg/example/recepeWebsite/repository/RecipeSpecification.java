package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.dto.SearchRecipeDto;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RecipeSpecification implements Specification<RecipeEntity> {

    private final SearchRecipeDto searchRecipeDto;

    public RecipeSpecification(SearchRecipeDto searchRecipeDto) {
        this.searchRecipeDto = searchRecipeDto;
    }

    @Override
    public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Predicate p = cb.conjunction();

        if (searchRecipeDto.getName() != null && !searchRecipeDto.getName().isEmpty()) {
            p.getExpressions().add(
                    cb.and(cb.like(root.get("name"), "%" + searchRecipeDto.getName() + "%")));
        }

        if (searchRecipeDto.getLevel() != null) {
            p.getExpressions().add(
                    cb.equal(root.get("level"), searchRecipeDto.getLevel()));
        }

        if (searchRecipeDto.getCategory() != null) {
            p.getExpressions().add(
                    cb.equal(root.get("category"), searchRecipeDto.getCategory()));
        }

        if (searchRecipeDto.getMinTimeNeeded() != null) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("timeNeeded"), searchRecipeDto.getMinTimeNeeded())));
        }

        if (searchRecipeDto.getMaxTimeNeeded() != null) {
            p.getExpressions().add(
                    cb.and(cb.lessThanOrEqualTo(root.get("timeNeeded"), searchRecipeDto.getMaxTimeNeeded())));
        }

        if (searchRecipeDto.getTypes() != null && !searchRecipeDto.getTypes().isEmpty()) {
            for (TypeNameEnum type : searchRecipeDto.getTypes()) {
                p.getExpressions().add(
                        cb.equal(root.join("types").get("name"), type));
            }
        }
        return p;
    }
}
