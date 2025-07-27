package com.example.learningkotlin.controller

import com.example.learningkotlin.dto.LoginRequest
import com.example.learningkotlin.dto.LoginResponse
import com.example.learningkotlin.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping()
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val token = authService.authenticate(request.login, request.password)
        return ResponseEntity.ok(LoginResponse(token))
    }
}
