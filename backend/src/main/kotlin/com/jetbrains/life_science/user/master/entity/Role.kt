package com.jetbrains.life_science.user.master.entity

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name = "roles")
class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val name: String

) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }
}
