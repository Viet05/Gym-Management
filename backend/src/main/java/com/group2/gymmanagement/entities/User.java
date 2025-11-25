package com.group2.gymmanagement.entities;


import com.group2.gymmanagement.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(name="username",nullable = false)
    private String userName;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "Phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updatedDate;
}
