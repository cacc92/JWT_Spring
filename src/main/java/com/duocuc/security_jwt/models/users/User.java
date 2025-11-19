package com.duocuc.security_jwt.models.users;

import com.duocuc.security_jwt.models.audit.Audit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "El campo username es obligatorio")
    @Size(min=6, max=50)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "El campo password es obligatorio")
    @Size(min=6, max=120)
    private String password;

    private Boolean enabled;

    // Esto hace que el dato no sea persistente de la base de datos si no solamente que viva
    // en la case que esto utilizando
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @Embedded
    // Este elemento me permite generar
    private Audit audit = new Audit();

    // Relación de muchos a muchos donde un rol puede tener muchos usuarios /
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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdAt=" + audit.getCreatedAt() +
                ", updatedAt=" + audit.getUpdatedAt() +
                '}';
    }

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
