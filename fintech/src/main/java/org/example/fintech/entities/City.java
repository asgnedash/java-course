package org.example.fintech.entities;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "City")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "city", nullable = false, length = 255)
    private String city;

}
