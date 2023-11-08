package bg.example.recepeWebsite.repository;

import bg.example.recepeWebsite.model.dto.SearchRecipeDto;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

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
                    cb.and(cb.equal(root.join("level"), searchRecipeDto.getLevel())));
        }

        if (searchRecipeDto.getCategory() != null) {
            p.getExpressions().add(
                    cb.and(cb.equal(root.join("category"), searchRecipeDto.getCategory())));
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
            List<TypeNameEnum> typeNames = searchRecipeDto.getTypes();

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<RecipeEntity> subRoot = subquery.from(RecipeEntity.class);
            Join<RecipeEntity, TypeEntity> subTypeJoin = subRoot.join("types");

            subquery.select(cb.count(subRoot));
            subquery.where(
                    cb.equal(subRoot.get("id"), root.get("id")),
                    subTypeJoin.get("name").in(typeNames)
            );

            p.getExpressions().add(
                    cb.equal(subquery, (long) typeNames.size())
            );
        }


        return p;
    }
}
