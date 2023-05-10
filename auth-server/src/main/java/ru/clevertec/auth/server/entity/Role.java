package ru.clevertec.auth.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * This class represents a security role that implements the {@link GrantedAuthority} interface.
 * A role is associated with a collection of users that are granted permission based on the role.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    /**
     * The unique identifier for the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The authority for the role.
     */
    @Column(nullable = false, unique = true)
    private String authority;

    /**
     * The collection of users associated with the role.
     */
    @ManyToMany(mappedBy = "authorities")
    private Collection<User> users;

}

