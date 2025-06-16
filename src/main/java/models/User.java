package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "getAllUsers",
        query = "SELECT u FROM User AS u"
    ),
    @NamedQuery(
            name = "userLogin",
            query = "SELECT u FROM User AS u where u.account= :account AND u.password = :password"
    ),
    @NamedQuery(
            name = "accountCheck",
            query = "SELECT u FROM User AS u where u.account= :account"
    ),
    @NamedQuery(
            name = "nicknameCheck",
            query = "SELECT u FROM User AS u where u.username= :nickname"
    ),
})
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "account", length = 30, nullable = false)
    private String account;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
