package com.example.learningkotlin.repository

import com.example.learningkotlin.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): User?
}