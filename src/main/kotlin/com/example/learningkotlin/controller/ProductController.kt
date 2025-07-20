package com.example.learningkotlin.controller

import com.example.learningkotlin.dto.ProductDTO
import com.example.learningkotlin.model.Product
import com.example.learningkotlin.service.ProductService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAll(): List<Product> = productService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") id: Long): Product = productService.getById(id)

    @PostMapping()
    fun create(@RequestBody dto: ProductDTO) = productService.create(dto)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = productService.delete(id)
}