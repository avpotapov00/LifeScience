package com.jetbrains.life_science.user.details.factory

import com.jetbrains.life_science.user.details.entity.AddDetailsInfo
import com.jetbrains.life_science.user.details.entity.User
import org.springframework.stereotype.Component

@Component
class UserFactory {
    fun create(firstName: String, lastName: String): User {
        return User(
            id = 0,
            firstName = firstName,
            lastName = lastName,
            organisations = mutableListOf(),
            positions = mutableListOf()
        )
    }

    fun setParams(user: User, addInfo: AddDetailsInfo): User {
        user.academicDegree = addInfo.academicDegree
        user.doctorDegree = addInfo.doctorDegree
        user.orcid = addInfo.orcid
        user.researchId = addInfo.researchId
        user.organisations = addInfo.organisations
        return user
    }
}
