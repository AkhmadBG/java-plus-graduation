package ru.practicum.ewm.stats.analyzer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "similarities")
public class Similarity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event1")
    private Long event1;

    @Column(name = "event2")
    private Long event2;

    @Column(name = "similarity")
    private Double similarity;

    @Column(name = "ts")
    private LocalDateTime created;

}