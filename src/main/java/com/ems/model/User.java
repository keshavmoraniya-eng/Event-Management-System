package com.ems.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",unique = true,nullable = false)
    @Size(max = 50)
    @NotBlank
    private String username;

    @Column(name = "email",unique = true,nullable = false)
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Size(max = 10)
    private String phoneNumber;

    @Column(name = "is_active")
    private Boolean isActive=true;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private Set<Role> roles=new HashSet<>();




}
