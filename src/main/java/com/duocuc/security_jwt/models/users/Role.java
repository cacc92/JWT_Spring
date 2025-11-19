package com.duocuc.security_jwt.models.users;

import com.duocuc.security_jwt.models.audit.Audit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name="roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique=true, nullable = false)
    private String name;

    @Embedded
    private Audit audit = new Audit();

    // Se genera la relación inversa entre los usuarios y los roles
    @JsonIgnoreProperties({"roles"})
    @ManyToMany(mappedBy = "roles")
    private List<User> users;


    public Role() {
        this.users = new ArrayList<>();
    }

    public Role(String name) {
        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + audit.getCreatedAt() +
                ", updatedAt=" + audit.getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
