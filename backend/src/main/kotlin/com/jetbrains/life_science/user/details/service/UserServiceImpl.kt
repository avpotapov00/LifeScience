package com.jetbrains.life_science.user.details.service

import com.jetbrains.life_science.exception.UserNotFoundException
import com.jetbrains.life_science.user.credentials.service.UserCredentialsService
import com.jetbrains.life_science.user.details.entity.User
import com.jetbrains.life_science.user.details.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    val userCredentialsService: UserCredentialsService,
    val userRepository: UserRepository
) : UserService {

    override fun getByEmail(email: String): User {
        return userCredentialsService.getByEmail(email).user
    }

    override fun getById(id: Long): User {
        return userRepository.findById(id).orElseThrow { UserNotFoundException("user not found by id $id") }
    }
}
