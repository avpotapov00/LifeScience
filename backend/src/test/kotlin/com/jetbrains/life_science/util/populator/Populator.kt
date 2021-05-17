package com.jetbrains.life_science.util.populator

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.reindex.DeleteByQueryRequest

internal class Populator(
    private val client: RestHighLevelClient,
    private val indexName: String,
    objectData: List<*>,
) {

    private val objectMapper = jacksonObjectMapper()

    private val dataAsStringList: List<String> = objectData.map { objectMapper.writeValueAsString(it) }

    fun prepareData() {
        clear()
        populate()
    }

    private fun clear() {
        val request = DeleteByQueryRequest(indexName).setQuery(QueryBuilders.matchAllQuery())
        val deleteByQuery = client.deleteByQuery(request, RequestOptions.DEFAULT)
        println("hmmm ${deleteByQuery.total}")
    }

    fun createIndex() {
        val request = CreateIndexRequest(indexName)
        client.indices().create(request, RequestOptions.DEFAULT)
    }

    private fun populate() = dataAsStringList.forEach { content ->
        val savingObjectMap = objectMapper.readValue(content, Map::class.java)
        val request = IndexRequest(indexName).source(content, XContentType.JSON)
        val id = savingObjectMap["id"]
        if (id != null) {
            request.id(id.toString())
        }
        client.index(request, RequestOptions.DEFAULT)
    }
}
