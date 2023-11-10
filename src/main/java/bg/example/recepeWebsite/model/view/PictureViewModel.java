package bg.example.recepeWebsite.model.view;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;

public class PictureViewModel {
    private Long id;
    private String url;
    private Long recipeId;
    String authorUsername;
    private boolean canNotDelete;

    public Long getId() {
        return id;
    }

    public PictureViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PictureViewModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public PictureViewModel setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public boolean isCanNotDelete() {
        return canNotDelete;
    }

    public PictureViewModel setCanNotDelete(boolean canNotDelete) {
        this.canNotDelete = canNotDelete;
        return this;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public PictureViewModel setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
        return this;
    }
}
