package com.example.learningkotlin.security

import com.example.learningkotlin.repository.SessionRepository
import com.example.learningkotlin.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return path.startsWith("/public/")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
            return
        }

        val session = sessionRepository.findByToken(token)
        if (session == null || session.expiresAt.isBefore(LocalDateTime.now())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired or not found")
            return
        }

        val userId = jwtUtil.getUserIdFromToken(token)
        val user = userRepository.findById(userId).orElse(null)
        if (user != null) {
            val authentication = UsernamePasswordAuthenticationToken(user, null, emptyList())
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}
