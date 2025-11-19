package com.duocuc.security_jwt.models.users;

import com.duocuc.security_jwt.models.audit.Audit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    // @ManyToMany(mappedBy = "roles")
    // @JsonBackReference
    // private Set<User> users;


    public Role() {
        // this.users = new HashSet<>();
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

}
