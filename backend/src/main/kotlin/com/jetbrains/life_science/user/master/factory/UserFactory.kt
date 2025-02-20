package com.jetbrains.life_science.user.master.factory

import com.jetbrains.life_science.user.master.entity.Role
import com.jetbrains.life_science.user.master.entity.User
import com.jetbrains.life_science.user.master.service.UpdateDetailsInfo
import com.jetbrains.life_science.user.master.service.NewUserInfo
import com.jetbrains.life_science.user.organisation.entity.Organisation
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserFactory(val encoder: PasswordEncoder) {

    fun create(info: NewUserInfo, roles: MutableCollection<Role>): User {
        val password = encoder.encode(info.password)
        return User(
            id = 0,
            email = info.email,
            password = password,
            roles = roles,
            firstName = info.firstName,
            lastName = info.lastName,
            organisations = mutableListOf(),
            positions = mutableListOf(),
            favouriteArticles = mutableListOf()
        )
    }

    fun setParams(updateInfo: UpdateDetailsInfo, organisations: List<Organisation>, user: User): User {
        user.academicDegree = updateInfo.academicDegree
        user.doctorDegree = updateInfo.doctorDegree
        user.orcid = updateInfo.orcid
        user.researchId = updateInfo.researchId
        user.organisations = organisations.toMutableList()
        return user
    }
}
