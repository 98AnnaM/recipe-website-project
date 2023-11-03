package bg.example.recepeWebsite.model.entity;

import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "types")
public class TypeEntity extends BaseEntity {

    private TypeNameEnum name;
    private List<RecipeEntity> recipes;

    public TypeNameEnum getName() {
        return name;
    }

    public TypeEntity setName(TypeNameEnum name) {
        this.name = name;
        return this;
    }

    @OneToMany
    public List<RecipeEntity> getRecipes() {
        return recipes;
    }

    public TypeEntity setRecipes(List<RecipeEntity> recipes) {
        this.recipes = recipes;
        return this;
    }
}
