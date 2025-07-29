package com.example.learningkotlin.service

import com.example.learningkotlin.model.Session
import com.example.learningkotlin.repository.SessionRepository
import com.example.learningkotlin.repository.UserRepository
import com.example.learningkotlin.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder
) {

    fun authenticate(login: String, password: String): String {
        val user = userRepository.findByLogin(login)
            ?: throw RuntimeException("User not found")

        if (user.password != password) {
            throw RuntimeException("Invalid password")
        }

        val token = jwtUtil.generateToken(user.id)
        val session = Session(
            user = user,
            token = token,
            expiresAt = LocalDateTime.now().plusMinutes(60)
        )
        sessionRepository.save(session)

        return token
    }
}
