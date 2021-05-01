package com.jetbrains.life_science.search.result.content

import com.jetbrains.life_science.search.query.SearchUnitType
import com.jetbrains.life_science.search.result.UnitSearchService
import com.jetbrains.life_science.util.getOrThrow
import org.springframework.stereotype.Service

@Service
class ContentSearchService : UnitSearchService(SearchUnitType.CONTENT) {

    override fun process(id: String, response: Map<String, Any>): ContentSearchResult {
        val text = response.getOrThrow("text") { "Text not found" }.toString()
        return ContentSearchResult(id, text)
    }
}
