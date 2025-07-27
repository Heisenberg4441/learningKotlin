package com.example.learningkotlin.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sessions")
data class Session(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(columnDefinition = "TEXT")
    val token: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    val expiresAt: LocalDateTime
)
