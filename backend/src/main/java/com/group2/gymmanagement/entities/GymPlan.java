package com.group2.gymmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gym_plan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GymPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_month")
    private Integer duration;

    @Column(nullable = false)
    private Long price;

    private Integer status; // 1: Active, 0: Inactive
}
