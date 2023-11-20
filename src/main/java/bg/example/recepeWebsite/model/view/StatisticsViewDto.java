package bg.example.recepeWebsite.model.view;

import java.time.LocalDateTime;

public class StatisticsViewDto {

    private long allRecipes;
    private long veganRecipes;
    private long vegetarianRecipes;
    private long meatRecipes;
    private long usersCount;
    private LocalDateTime localDateTime;

    public long getAllRecipes() {
        return allRecipes;
    }

    public StatisticsViewDto setAllRecipes(long allRecipes) {
        this.allRecipes = allRecipes;
        return this;
    }

    public long getVeganRecipes() {
        return veganRecipes;
    }

    public StatisticsViewDto setVeganRecipes(long veganRecipes) {
        this.veganRecipes = veganRecipes;
        return this;
    }

    public long getVegetarianRecipes() {
        return vegetarianRecipes;
    }

    public StatisticsViewDto setVegetarianRecipes(long vegetarianRecipes) {
        this.vegetarianRecipes = vegetarianRecipes;
        return this;
    }

    public long getMeatRecipes() {
        return meatRecipes;
    }

    public StatisticsViewDto setMeatRecipes(long meatRecipes) {
        this.meatRecipes = meatRecipes;
        return this;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public StatisticsViewDto setUsersCount(long usersCount) {
        this.usersCount = usersCount;
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
