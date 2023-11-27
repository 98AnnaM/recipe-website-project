package bg.example.recepeWebsite.model.entity;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes")
public class RecipeEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Lob
    private String products;
    @Column(nullable = false)
    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelEnum level;
    @ManyToOne(optional = false)
    private UserEntity author;
    @Column
    private String videoUrl;
    @OneToMany(mappedBy = "recipe")
    private List<PictureEntity> pictures;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TypeEntity> types;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryNameEnum category;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> comments;
    @Column(nullable = false)
    private Integer timeNeeded;
    @Column(nullable = false)
    private Integer portions;
    @ManyToMany(mappedBy = "favorites")
    private List<UserEntity> favoriteUsers;

    public RecipeEntity() {
    }

    public String getName() {
        return name;
    }

    public RecipeEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getProducts() {
        return products;
    }

    public RecipeEntity setProducts(String products) {
        this.products = products;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecipeEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RecipeEntity setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public RecipeEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RecipeEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public RecipeEntity setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
        return this;
    }

    public List<TypeEntity> getTypes() {
        return types;
    }

    public RecipeEntity setTypes(List<TypeEntity> types) {
        this.types = types;
        return this;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public RecipeEntity setCategory(CategoryNameEnum category) {
        this.category = category;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public RecipeEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public Integer getTimeNeeded() {
        return timeNeeded;
    }

    public RecipeEntity setTimeNeeded(Integer timeNeeded) {
        this.timeNeeded = timeNeeded;
        return this;
    }

    public Integer getPortions() {
        return portions;
    }

    public RecipeEntity setPortions(Integer portions) {
        this.portions = portions;
        return this;
    }

    public List<UserEntity> getFavoriteUsers() {
        return favoriteUsers;
    }

    public RecipeEntity setFavoriteUsers(List<UserEntity> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
        return this;
    }
}
