package com.secrets_as_a_service.springvaultdatabaseshutdown.controller

import com.secrets_as_a_service.springvaultdatabaseshutdown.entity.Customer
import com.secrets_as_a_service.springvaultdatabaseshutdown.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
class Controller(
        private val userRepository: UserRepository,
        private val dataSource: DataSource
) {
    @GetMapping("/customers")
    fun getCustomers(): List<Customer> = userRepository.findAll()

    @GetMapping("/dbuser")
    fun getDbUser(): String = dataSource.connection.use { connection ->
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT CURRENT_USER;").apply { next() }.use {
                it.getString(1)
            }
        }
    }
}