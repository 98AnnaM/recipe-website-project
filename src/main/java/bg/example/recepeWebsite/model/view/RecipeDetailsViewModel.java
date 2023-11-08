package bg.example.recepeWebsite.model.view;

import bg.example.recepeWebsite.model.entity.*;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;

import java.util.List;

public class RecipeDetailsViewModel {

    private Long id;
    private String name;
    private List<String> products;
    private String description;
    private LevelEnum level;
    private String author;
    private String videoId;
    private List<PictureViewModel> pictures;
    private List<CommentEntity> comments;
    private Integer timeNeeded;
    private Integer portions;
    private boolean canDelete;

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private CategoryNameEnum category;

    public CategoryNameEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryNameEnum category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public RecipeDetailsViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public RecipeDetailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RecipeDetailsViewModel setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public RecipeDetailsViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getVideoId() {
        return videoId;
    }

    public RecipeDetailsViewModel setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public List<PictureViewModel> getPictures() {
        return pictures;
    }

    public RecipeDetailsViewModel setPictures(List<PictureViewModel> pictures) {
        this.pictures = pictures;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public RecipeDetailsViewModel setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public Integer getTimeNeeded() {
        return timeNeeded;
    }

    public void setTimeNeeded(Integer timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    public Integer getPortions() {
        return portions;
    }

    public void setPortions(Integer portions) {
        this.portions = portions;
    }
}
