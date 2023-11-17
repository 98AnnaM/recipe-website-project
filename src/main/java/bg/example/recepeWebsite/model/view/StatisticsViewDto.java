package bg.example.recepeWebsite.model.view;

import java.time.LocalDateTime;

public class StatisticsViewDto {

    private long allRecipes;
    private int veganRecipes;
    private int vegetarianRecipes;
    private int meatRecipes;
    private LocalDateTime localDateTime;

    public long getAllRecipes() {
        return allRecipes;
    }

    public StatisticsViewDto setAllRecipes(long allRecipes) {
        this.allRecipes = allRecipes;
        return this;
    }

    public int getVeganRecipes() {
        return veganRecipes;
    }

    public StatisticsViewDto setVeganRecipes(int veganRecipes) {
        this.veganRecipes = veganRecipes;
        return this;
    }

    public int getVegetarianRecipes() {
        return vegetarianRecipes;
    }

    public StatisticsViewDto setVegetarianRecipes(int vegetarianRecipes) {
        this.vegetarianRecipes = vegetarianRecipes;
        return this;
    }

    public int getMeatRecipes() {
        return meatRecipes;
    }

    public StatisticsViewDto setMeatRecipes(int meatRecipes) {
        this.meatRecipes = meatRecipes;
        return this;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public StatisticsViewDto setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        return this;
    }
}
