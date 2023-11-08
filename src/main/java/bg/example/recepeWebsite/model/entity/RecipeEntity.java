package bg.example.recepeWebsite.model.entity;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipes")
public class RecipeEntity extends BaseEntity {

    private String name;
    private String products;
    private String description;
    private LevelEnum level;
    private UserEntity author;
    private String videoUrl;
    private List<PictureEntity> pictures;
    private List<TypeEntity> types;
    private CategoryNameEnum category;
    private List<CommentEntity> comments;
    private Integer timeNeeded;
    private Integer portions;

    public RecipeEntity() {
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    @Column(nullable = false, columnDefinition = "TEXT")
    public String getProducts() {
        return products;
    }

    @Column(nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public LevelEnum getLevel() {
        return level;
    }

    @ManyToOne(optional = false)
    public UserEntity getAuthor() {
        return author;
    }

    @Column
    public String getVideoUrl() {
        return videoUrl;
    }

    @ManyToMany(mappedBy = "recipes",fetch = FetchType.LAZY)
    public List<TypeEntity> getTypes() {
        return types;
    }

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    public List<PictureEntity> getPictures() {
        return pictures;
    }

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<CommentEntity> getComments() {
        return comments;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public CategoryNameEnum getCategory() {
        return category;
    }

    @Column(nullable = false)
    public Integer getTimeNeeded() {
        return timeNeeded;
    }

    @Column(nullable = false)
    public Integer getPortions() {
        return portions;
    }

    public RecipeEntity setName(String name) {
        this.name = name;
        return this;
    }

    public RecipeEntity setProducts(String products) {
        this.products = products;
        return this;
    }

    public RecipeEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public RecipeEntity setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public RecipeEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public RecipeEntity setVideoUrl(String video_url) {
        this.videoUrl = video_url;
        return this;
    }

    public RecipeEntity setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
        return this;
    }

    public RecipeEntity setTypes(List<TypeEntity> types) {
        this.types = types;
        return this;
    }

    public RecipeEntity setCategory(CategoryNameEnum category) {
        this.category = category;
        return this;
    }

    public RecipeEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public RecipeEntity setTimeNeeded(Integer timeNeeded) {
        this.timeNeeded = timeNeeded;
        return this;
    }

    public RecipeEntity setPortions(Integer portions) {
        this.portions = portions;
        return this;
    }
}
