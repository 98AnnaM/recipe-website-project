package bg.example.recepeWebsite.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {

    private String url;
    private UserEntity author;
    private String publicId;
    private String title;

    public PictureEntity() {
    }


    @Column
    public String getPublicId() {
        return publicId;
    }

    @Column(nullable = false, columnDefinition = "TEXT")
    public String getUrl() {
        return url;
    }

    @ManyToOne(optional = false)
    public UserEntity getAuthor() {
        return author;
    }



    @Column
    public String getTitle() {
        return title;
    }

    public PictureEntity setTitle(String name) {
        this.title = name;
        return this;
    }

    public PictureEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public PictureEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }



    public PictureEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }
}
