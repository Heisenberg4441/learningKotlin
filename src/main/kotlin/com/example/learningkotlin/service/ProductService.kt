package com.example.learningkotlin.service

import com.example.learningkotlin.dto.ProductDTO
import com.example.learningkotlin.model.Product
import com.example.learningkotlin.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val repository: ProductRepository,
) {
    fun getAll(): List<Product> = repository.findAll()

    fun getById(id: Long): Product =
        repository.findById(id).orElseThrow { RuntimeException("Product not found") }

    fun create(dto: ProductDTO): Product {
        val product = Product(
            name = dto.name,
            description = dto.description,
            price = dto.price
        )
        return repository.save(product)
    }

    fun delete(id: Long) {
        return repository.deleteById(id)
    }
}