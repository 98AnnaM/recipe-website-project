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
    private int timeNeeded;

    @Positive
    private int portions;

    public AddRecipeDto() {
        this.pictureFiles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public AddRecipeDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getProducts() {
        return products;
    }

    public AddRecipeDto setProducts(String products) {
        this.products = products;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AddRecipeDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public AddRecipeDto setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public AddRecipeDto setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public List<MultipartFile> getPictureFiles() {
        return pictureFiles;
    }

    public AddRecipeDto setPictureFiles(List<MultipartFile> pictureFiles) {
        this.pictureFiles = pictureFiles;
        return this;
    }

    public List<TypeNameEnum> getTypes() {
        return types;
    }

    public AddRecipeDto setTypes(List<TypeNameEnum> types) {
        this.types = types;
        return this;
    }

    public CategoryNameEnum getCategory() {
        return category;
    }

    public AddRecipeDto setCategory(CategoryNameEnum category) {
        this.category = category;
        return this;
    }

    public int getTimeNeeded() {
        return timeNeeded;
    }

    public AddRecipeDto setTimeNeeded(int timeNeeded) {
        this.timeNeeded = timeNeeded;
        return this;
    }

    public int getPortions() {
        return portions;
    }

    public AddRecipeDto setPortions(int portions) {
        this.portions = portions;
        return this;
    }
}
