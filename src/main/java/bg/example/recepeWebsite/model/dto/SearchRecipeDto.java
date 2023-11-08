package bg.example.recepeWebsite.model.dto;

import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;

import java.util.List;
import java.util.stream.Collectors;

public class SearchRecipeDto {

    private String name;
    private LevelEnum level;
    private List<TypeEntity> types;
    private CategoryNameEnum category;
    private Integer minTimeNeeded;
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

    public List<TypeEntity> getTypes() {
        return types;
    }

    public void setTypes(List<TypeEntity> types) {
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

        if (types != null){
            List<String> typeNames = types.stream()
                    .map(typeEntity -> typeEntity.getName().name())
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
