package com.secrets_as_a_service.springvaultdatabaseshutdown.repository

import com.secrets_as_a_service.springvaultdatabaseshutdown.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<Customer, Int>