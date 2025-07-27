package com.example.learningkotlin.repository

import com.example.learningkotlin.model.Session
import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<Session, Long> {
    fun findByToken(token: String): Session?
}