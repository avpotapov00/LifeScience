package com.jetbrains.life_science.article.version.dto

import com.jetbrains.life_science.article.master.entity.Article
import com.jetbrains.life_science.article.version.service.ArticleVersionCreationInfo
import com.jetbrains.life_science.user.master.entity.User

class ArticleVersionCreationDTOToInfoAdapter(
    val dto: ArticleVersionCreationDTO,
    override val user: User,
    override val article: Article,
) : ArticleVersionCreationInfo {
    override val name: String = dto.name
}
