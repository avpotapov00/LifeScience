package com.jetbrains.life_science.article.section.repository

import com.jetbrains.life_science.article.section.entity.Section
import com.jetbrains.life_science.article.version.entity.ArticleVersion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : JpaRepository<Section, Long> {

    fun findAllByArticleVersion(articleVersion: ArticleVersion): List<Section>

    fun findAllByArticleVersionId(articleVersionId: Long): List<Section>
}
