package com.jetbrains.life_science.article.version.service

import com.jetbrains.life_science.article.master.service.ArticleService
import com.jetbrains.life_science.exception.ArticleVersionNotFoundException
import com.jetbrains.life_science.exception.PublishedVersionNotFoundException
import com.jetbrains.life_science.section.service.SectionService
import com.jetbrains.life_science.article.version.adapter.SectionEmptyCreationToInfoAdapter
import com.jetbrains.life_science.article.version.entity.ArticleVersion
import com.jetbrains.life_science.article.version.entity.State
import com.jetbrains.life_science.article.version.factory.ArticleVersionFactory
import com.jetbrains.life_science.article.version.repository.ArticleVersionRepository
import com.jetbrains.life_science.article.version.search.service.ArticleVersionSearchUnitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleVersionServiceImpl(
    val repository: ArticleVersionRepository,
    val factory: ArticleVersionFactory,
    val articleService: ArticleService,
    val searchService: ArticleVersionSearchUnitService
) : ArticleVersionService {

    @Autowired
    lateinit var sectionService: SectionService

    @Transactional
    override fun createBlank(info: ArticleVersionInfo): ArticleVersion {
        val method = articleService.getById(info.articleId)
        var methodVersion = factory.create(info, method)
        methodVersion = repository.save(methodVersion)
        sectionService.createBlankByVersion(SectionEmptyCreationToInfoAdapter(methodVersion))
        return methodVersion
    }

    @Transactional
    override fun createCopy(articleId: Long) {
        val publishedVersion = getPublishedVersion(articleId)
        var copy = factory.createCopy(publishedVersion)
        copy = repository.save(copy)
        sectionService.createCopiesByArticle(publishedVersion, copy)
    }

    @Transactional
    override fun getPublishedVersion(articleId: Long): ArticleVersion {
        return (
            repository.findByMainArticleIdAndState(articleId, State.PUBLISHED)
                ?: throw PublishedVersionNotFoundException("published version to method $articleId not found")
            )
    }

    @Transactional
    override fun approve(id: Long) {
        val currentVersion = getVersionById(id)
        val lastPublished = repository.findByMainArticleIdAndState(currentVersion.mainArticle.id, State.PUBLISHED)
        currentVersion.state = State.PUBLISHED
        if (lastPublished != null) {
            lastPublished.state = State.ARCHIVED
            searchService.deleteSearchUnitById(lastPublished.id)
            sectionService.deleteSearchUnits(lastPublished.sections)
        }
        searchService.createSearchUnit(currentVersion)
        sectionService.createSearchUnits(currentVersion.sections)
    }

    override fun getById(id: Long): ArticleVersion {
        checkExistsById(id)
        return repository.getOne(id)
    }

    @Transactional
    override fun getVersionById(id: Long): ArticleVersion {
        return repository.findById(id)
            .orElseThrow { ArticleVersionNotFoundException("Article version with id: $id not found") }
    }

    private fun checkExistsById(id: Long) {
        if (repository.existsById(id)) {
            throw ArticleVersionNotFoundException("Article version with id: $id not found")
        }
    }
}
