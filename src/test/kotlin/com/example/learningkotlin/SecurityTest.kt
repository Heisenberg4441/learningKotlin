package com.example.learningkotlin

import com.example.learningkotlin.model.User
import com.example.learningkotlin.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "jwt.secret=very-long-and-secure-jwt-secret-key-for-testing-purposes-only",
    "jwt.expiration=3600000"
])
class SecurityTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply { org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity() }
            .build()

        // Очищаем базу данных и создаем тестового пользователя
        userRepository.deleteAll()
        
        val testUser = User(
            login = "testuser",
            password = "testpass", // Пароль без шифрования
            name = "Test User"
        )
        userRepository.save(testUser)
    }

    @Test
    fun `login endpoint should return 200 OK`() {
        val loginRequest = """
            {
                "login": "testuser",
                "password": "testpass"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest)
        ).andExpect { result ->
            // Проверяем, что получаем 200 (OK)
            assert(result.response.status == 200) {
                "Login endpoint should return 200 OK, but got ${result.response.status}"
            }
        }
    }
} 