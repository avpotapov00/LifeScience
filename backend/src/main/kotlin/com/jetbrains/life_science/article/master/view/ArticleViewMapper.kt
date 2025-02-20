package com.jetbrains.life_science.article.master.view

import com.jetbrains.life_science.article.master.entity.Article
import com.jetbrains.life_science.article.version.entity.State
import com.jetbrains.life_science.article.version.view.ArticleVersionViewMapper
import org.springframework.stereotype.Component

@Component
class ArticleViewMapper(
    val versionViewMapper: ArticleVersionViewMapper
) {
    fun createView(article: Article): ArticleView {
        val publishedVersionView = article.versions
            .filter { it.state == State.PUBLISHED_AS_ARTICLE }
            .map { versionViewMapper.toView(it) }
            .firstOrNull()
        val protocolsView = article.versions
            .filter { it.state == State.PUBLISHED_AS_PROTOCOL }
            .map { versionViewMapper.toView(it) }
        return ArticleView(article.id, publishedVersionView, protocolsView)
    }
}
