package bg.example.recepeWebsite.model.view;

import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;

public class RecipeViewModel {

    private Long id;
    private String name;
    private LevelEnum level;
    private CategoryNameEnum category;
    private String author;
    private String pictureUrl;
    private int timeNeeded;
    private int portions;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RecipeViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryNameEnum category) {
        this.category = category;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RecipeViewModel setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public RecipeViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public RecipeViewModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public int getTimeNeeded() {
        return timeNeeded;
    }

    public RecipeViewModel setTimeNeeded(int timeNeeded) {
        this.timeNeeded = timeNeeded;
        return this;
    }

    public int getPortions() {
        return portions;
    }

    public RecipeViewModel setPortions(int portions) {
        this.portions = portions;
        return this;
    }
}
