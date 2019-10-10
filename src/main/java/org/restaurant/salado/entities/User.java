package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, insertable = true, updatable = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "image")
    private String image;

    @Column(name = "location")
    private String location;

    @Column(name = "token", unique = true, insertable = true, updatable = true)
    @JsonIgnore
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date")
    @JsonIgnore
    private Date expiryDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("users")
    private Collection<Role> roles;

    // Convenient method to add new roles
    public void addRole(Role role) {
        if( this.roles == null ) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    // Calculate expiry date
    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    // Check token validity
    @JsonIgnore
    public boolean isValidToken() {
        return this.expiryDate != null && this.expiryDate.getTime() > new Date().getTime();
    }

}
