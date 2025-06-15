package models;

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
        name = "getLikeCounts",
        query = "SELECT COUNT(l) FROM Like AS l where l.photo.id = :photo_id"
    ),
    @NamedQuery(
            name = "getLikeCheck",
            query = "SELECT l FROM Like AS l where l.user.id = :user_id and l.photo.id = :photo_id"
        ),
    @NamedQuery(
            name = "getLikes",
            query = "SELECT l FROM Like AS l where l.photo.id = :photo_id"
        ),
})
@Table(name = "likes")
public class Like {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
    
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

}
