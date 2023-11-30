package bg.example.recepeWebsite.model.view;

public class PictureHomePageViewModel {

    private String url;
    private Long recipeId;
    String authorFullName;

    public String getUrl() {
        return url;
    }

    public PictureHomePageViewModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public PictureHomePageViewModel setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public PictureHomePageViewModel setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
        return this;
    }
}
