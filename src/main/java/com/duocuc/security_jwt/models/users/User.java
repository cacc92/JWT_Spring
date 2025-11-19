package com.duocuc.security_jwt.models.users;

import com.duocuc.security_jwt.models.audit.Audit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    // Esto hace que el dato no sea persistente de la base de datos si no solamente que viva
    // en la case que esto utilizando
    @Transient
    private boolean admin;


    @Embedded
    // Este elemento me permite generar
    private Audit audit = new Audit();

    // Relación de muchos a muchos donde un rol puede tener muchos usuarios /
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            // Le doy el nombre de la tabla intermedia que tomará el ORM
            name = "users_roles",
            // Cúal es la columna principal que estoy uniendo
            joinColumns = @JoinColumn(name="user_id"),
            // Cúal es la columna de la tabla inversa
            inverseJoinColumns = @JoinColumn(name="role_id"),
            // se genera la restricción de la llaves para que ambas llaves pasen a ser la llave única
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})}
    )
    @JsonManagedReference
    private List<Role> roles;


    public User() {
        this.roles = new ArrayList<>();
    }

    public User(String username,
                String password
    ) {
        this();
        this.username = username;
        this.password = password;
        this.enabled = true;
    }

    // public void addRole(Role profile) {
    //     this.roles.add(profile);
    //     profile.getUsers().add(this);
    // }

    // public void removeRole(Role profile) {
    //    this.roles.remove(profile);
    //     profile.getUsers().remove(this);
    // }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdAt=" + audit.getCreatedAt() +
                ", updatedAt=" + audit.getUpdatedAt() +
                '}';
    }
}
