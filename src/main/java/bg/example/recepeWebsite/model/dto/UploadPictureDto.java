package bg.example.recepeWebsite.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadPictureDto {

    private Long recipeId;
    private MultipartFile picture;

    public MultipartFile getPicture() {
        return picture;
    }

    public UploadPictureDto setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public UploadPictureDto setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }
}


