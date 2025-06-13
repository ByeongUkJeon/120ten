package models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "getAllPhotos",
        query = "SELECT p FROM Photo AS p order by p.createdAt DESC"
    ),
    @NamedQuery(
            name = "getPhotoToday",
            query = "SELECT p FROM Photo AS p where p.createdAt >= :start AND p.createdAt < :end"
    ),
    @NamedQuery(
            name = "getPhotoCount",
            query = "SELECT COUNT(p) FROM Photo AS p"
    ),
    @NamedQuery(
            name = "getRankingPhotos",
            query = "SELECT p FROM Photo AS p where p.createdAt >= :start AND p.createdAt < :end order by p.score DESC"
    )
})
@Table(name = "photos")
public class Photo {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "image_path", length = 255)
    private String imagePath;
    
    @Column(name = "score")
    private Integer score;
    
    @Column(name = "label")
    private String label;
    
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "likecount", nullable = false)
    private Integer likeCount;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
    
}
