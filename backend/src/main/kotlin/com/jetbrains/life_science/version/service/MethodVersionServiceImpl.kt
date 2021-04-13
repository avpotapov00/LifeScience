package com.jetbrains.life_science.version.service

import com.jetbrains.life_science.container.service.ContainerService
import com.jetbrains.life_science.exception.MethodVersionNotFoundException
import com.jetbrains.life_science.exception.PublishedVersionNotFoundException
import com.jetbrains.life_science.method.service.MethodService
import com.jetbrains.life_science.version.adapter.ContainerEmptyCreationToInfoAdapter
import com.jetbrains.life_science.version.entity.MethodVersion
import com.jetbrains.life_science.version.entity.State
import com.jetbrains.life_science.version.factory.MethodVersionFactory
import com.jetbrains.life_science.version.repository.MethodVersionRepository
import com.jetbrains.life_science.version.search.service.MethodVersionSearchUnitService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MethodVersionServiceImpl(
    val repository: MethodVersionRepository,
    val factory: MethodVersionFactory,
    val methodService: MethodService,
    val containerService: ContainerService,
    val searchService: MethodVersionSearchUnitService
) : MethodVersionService {

    @Transactional
    override fun createBlank(info: MethodVersionInfo): MethodVersion {
        val method = methodService.getById(info.methodId)
        var methodVersion = factory.create(info, method)
        methodVersion = repository.save(methodVersion)
        containerService.createBlankByVersion(ContainerEmptyCreationToInfoAdapter(methodVersion))
        return methodVersion
    }

    @Transactional
    override fun createCopy(methodId: Long) {
        val publishedVersion = getPublishedVersion(methodId)
        var copy = factory.createCopy(publishedVersion)
        copy = repository.save(copy)
        containerService.createCopiesByMethod(publishedVersion, copy)
    }

    @Transactional
    override fun getPublishedVersion(methodId: Long): MethodVersion {
        return (
            repository.findByMainMethodIdAndState(methodId, State.PUBLISHED)
                ?: throw PublishedVersionNotFoundException("published version to method $methodId not found")
            )
    }

    @Transactional
    override fun approve(id: Long) {
        val currentVersion = getVersionById(id)
        val lastPublished = repository.findByMainMethodIdAndState(currentVersion.mainMethod.id, State.PUBLISHED)
        currentVersion.state = State.PUBLISHED
        if (lastPublished != null) {
            lastPublished.state = State.ARCHIVED
            searchService.deleteSearchUnitById(lastPublished.id)
            containerService.deleteSearchUnits(lastPublished.containers)
        }
        searchService.createSearchUnit(currentVersion)
        containerService.createSearchUnits(currentVersion.containers)
    }

    override fun getById(id: Long): MethodVersion {
        checkExistsById(id)
        return repository.getOne(id)
    }

    @Transactional
    override fun getVersionById(id: Long): MethodVersion {
        return repository.findById(id)
            .orElseThrow { MethodVersionNotFoundException(id) }
    }

    private fun checkExistsById(id: Long) {
        if (repository.existsById(id)) {
            throw MethodVersionNotFoundException(id)
        }
    }
}
