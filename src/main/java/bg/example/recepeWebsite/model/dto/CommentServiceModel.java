package bg.example.recepeWebsite.model.dto;

public class CommentServiceModel {

    private Long recipeId;
    private String message;
    private String creator;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long routeId) {
        this.recipeId = routeId;
    }
}
