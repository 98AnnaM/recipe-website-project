package bg.example.recepeWebsite.model.dto;

import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class SearchRecipeDto {

    @Size(max = 30)
    private String name;
    private LevelEnum level;
    private List<TypeNameEnum> types;
    private CategoryNameEnum category;
    @Positive
    private Integer minTimeNeeded;
    @Positive
    private Integer maxTimeNeeded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public List<TypeNameEnum> getTypes() {
        return types;
    }

    public void setTypes(List<TypeNameEnum> types) {
        this.types = types;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryNameEnum category) {
        this.category = category;
    }

    public Integer getMinTimeNeeded() {
        return minTimeNeeded;
    }

    public void setMinTimeNeeded(Integer minTimeNeeded) {
        this.minTimeNeeded = minTimeNeeded;
    }

    public Integer getMaxTimeNeeded() {
        return maxTimeNeeded;
    }

    public void setMaxTimeNeeded(Integer maxTimeNeeded) {
        this.maxTimeNeeded = maxTimeNeeded;
    }

    public boolean isEmpty() {
        return (name == null || name.isEmpty()) &&
                maxTimeNeeded == null &&
                minTimeNeeded == null &&
                level == null &&
                category == null &&
                (types == null || types.isEmpty());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

       if (name != null && !name.isEmpty()){
           sb.append(String.format("Name: " + name + " "));
       }

        if (level != null){
            sb.append(String.format("Level: " + level + " "));
        }

        if (types != null && !types.isEmpty()){
            List<String> typeNames = types.stream()
                    .map(TypeNameEnum::name)
                    .collect(Collectors.toList());

            String result = String.join(", ", typeNames);
            sb.append(String.format("Types: " + result + " "));
        }

        if (category != null){
            sb.append(String.format("Category: " + category + " "));
        }

        if (minTimeNeeded != null){
            sb.append(String.format("Min time needed: " + minTimeNeeded + "min "));
        }

        if (maxTimeNeeded != null){
            sb.append(String.format("Max time needed: " + maxTimeNeeded + "min "));
        }


        return sb.toString();
    }
}
