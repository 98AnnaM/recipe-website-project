package bg.example.recepeWebsite.model.dto;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.model.validation.AtLeastOneFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeDto {


    @AtLeastOneFile
    private List<MultipartFile> pictureFiles;

    @NotEmpty
    @Size(min = 5, max = 30)
    private String name;

    @NotEmpty
    private String products;

    @NotEmpty
    private String description;

    @NotNull
    private LevelEnum level;

    private String videoUrl;

    private List<TypeNameEnum> types;

    @NotNull
    private CategoryNameEnum category;

    @NotNull
    @Positive
    private Integer timeNeeded;

    @NotNull
    @Positive
    private Integer portions;

    public AddRecipeDto() {
        this.pictureFiles = new ArrayList<>();
    }

    public List<MultipartFile> getPictureFiles() {
        return pictureFiles;
    }

    public void setPictureFiles(List<MultipartFile> pictureFiles) {
        this.pictureFiles = pictureFiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<TypeNameEnum> getTypes() {
        return types;
    }

    public void setTypes(List<TypeNameEnum> types) {
        this.types = types;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryNameEnum category) {
        this.category = category;
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
