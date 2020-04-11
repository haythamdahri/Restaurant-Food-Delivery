package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Haytam DAHRI
 */
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = RestaurantFile.class)
    @JoinColumn(name = "image_id")
    private RestaurantFile image;

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

    @ManyToMany(targetEntity = Meal.class)
    @JoinTable(name="users_preferred_meals", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "meal_id"))
    private List<Meal> preferredMeals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Review> reviews;

    /**
     * Convenient method to add new roles
     * @param role
     */
    public void addRole(Role role) {
        if( this.roles == null ) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    /**
     * Expiration calculator
     * @param expiryTimeInMinutes
     * @return
     */
    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    /**
     * Token validation checker
     * @return
     */
    @JsonIgnore
    public boolean isValidToken() {
        return this.expiryDate != null && this.expiryDate.getTime() > new Date().getTime();
    }

    /**
     * Convenient method to add meal to user preferences
     * @param meal
     */
    public boolean addOrRemoveMealFromUserPreferences(Meal meal) {
        if( this.preferredMeals == null ) {
            return false;
        } else {
            if (this.preferredMeals.contains(meal)) {
                this.preferredMeals.remove(meal);
                return false;
            } else {
                this.preferredMeals.add(meal);
                return true;
            }
        }
    }


}
