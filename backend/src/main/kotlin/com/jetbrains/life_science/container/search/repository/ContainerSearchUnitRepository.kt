package com.jetbrains.life_science.container.search.repository

import com.jetbrains.life_science.container.search.ContainerSearchUnit
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ContainerSearchUnitRepository : ElasticsearchRepository<ContainerSearchUnit, Long>
