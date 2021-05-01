package com.jetbrains.life_science.search.result.article

import com.jetbrains.life_science.search.query.SearchUnitType
import com.jetbrains.life_science.search.result.UnitSearchService
import com.jetbrains.life_science.util.getOrThrow
import org.springframework.stereotype.Service

@Service
class ArticleSearchService : UnitSearchService(SearchUnitType.ARTICLE) {

    override fun process(id: String, response: Map<String, Any>): ArticleSearchResult {
        val name = response.getOrThrow("text") { "Name not found" }.toString()
        return ArticleSearchResult(id, name)
    }
}
