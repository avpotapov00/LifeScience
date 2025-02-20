package com.jetbrains.life_science.user.master.dto

import com.jetbrains.life_science.user.master.service.NewUserInfo

class NewUserDTOToInfoAdapter(
    private val dto: NewUserDTO
) : NewUserInfo {

    override val email: String
        get() = dto.email

    override val password: String
        get() = dto.password

    override val firstName: String
        get() = dto.firstName

    override val lastName: String
        get() = dto.lastName
}
