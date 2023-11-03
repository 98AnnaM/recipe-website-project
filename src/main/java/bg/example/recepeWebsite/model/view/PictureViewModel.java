package bg.example.recepeWebsite.model.view;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;

public class PictureViewModel {
    private Long id;
    private String title;
    private String url;
    private UserEntity author;
    private RecipeEntity recipe;
    private String publicId;
    private boolean canNotDelete;

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public PictureViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public PictureViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PictureViewModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public PictureViewModel setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public PictureViewModel setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public PictureViewModel setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public boolean isCanNotDelete() {
        return canNotDelete;
    }

    public PictureViewModel setCanNotDelete(boolean canNotDelete) {
        this.canNotDelete = canNotDelete;
        return this;
    }
}
