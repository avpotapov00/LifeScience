package com.jetbrains.life_science.article.content.version.service

import com.jetbrains.life_science.article.content.publish.entity.Content
import com.jetbrains.life_science.article.content.publish.service.ContentInfo

interface ContentVersionService {

    fun saveCopy(copy: Content)

    fun create(info: ContentInfo): Content

    fun findById(contentId: String?): Content

    fun update(info: ContentInfo): Content

    fun delete(contentId: String)

    fun findBySectionId(sectionId: Long): Content?

    fun deleteBySectionId(sectionId: Long)

    fun archiveBySectionId(sectionId: Long)
}
