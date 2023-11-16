package bg.example.recepeWebsite.model.entity;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false, columnDefinition = "CLOB")
    private String textContent;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @ManyToOne(optional = false)
    private UserEntity author;
    @ManyToOne(optional = false)
    private RecipeEntity recipe;

    public CommentEntity() {
    }



    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }


    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }


    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }


    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity route) {
        this.recipe = route;
    }
}
