package bg.example.recepeWebsite.model.entity;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    private String textContent;
    private LocalDateTime created;
    private UserEntity author;
    private RecipeEntity recipe;

    public CommentEntity() {
    }


    @Column(nullable = false, columnDefinition = "TEXT")
    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @Column(name = "created", nullable = false)
    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @ManyToOne(optional = false)
    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    @ManyToOne(optional = false)
    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity route) {
        this.recipe = route;
    }
}
