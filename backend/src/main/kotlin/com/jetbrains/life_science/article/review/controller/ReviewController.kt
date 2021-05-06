package com.jetbrains.life_science.article.review.controller

import com.jetbrains.life_science.article.review.dto.ReviewDTO
import com.jetbrains.life_science.article.review.dto.ReviewDTOToInfoAdapter
import com.jetbrains.life_science.article.review.service.ReviewService
import com.jetbrains.life_science.article.review.view.ReviewView
import com.jetbrains.life_science.article.review.view.ReviewViewMapper
import com.jetbrains.life_science.article.version.service.ArticleVersionService
import com.jetbrains.life_science.exception.not_found.ReviewNotFoundException
import com.jetbrains.life_science.user.master.service.UserCredentialsService
import com.jetbrains.life_science.util.email
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/articles/versions/{versionId}")
class ArticleReviewController(
    val reviewService: ReviewService,
    val articleVersionService: ArticleVersionService,
    val userCredentialsService: UserCredentialsService,
    val mapper: ReviewViewMapper,
) {

    @GetMapping("/reviews")
    fun getReviews(
        @PathVariable versionId: Long,
        principal: Principal
    ): List<ReviewView> {
        val user = userCredentialsService.getByEmail(principal.email)
        return reviewService.getAllByVersionId(versionId, user).map { mapper.createView(it) }
    }

    @GetMapping("/reviews/{reviewId}")
    fun getReview(
        @PathVariable versionId: Long,
        @PathVariable reviewId: Long,
        principal: Principal
    ): ReviewView {
        val user = userCredentialsService.getByEmail(principal.email)
        val review = reviewService.getById(reviewId, user)
        return mapper.createView(review)
    }

    @Secured("ROLE_MODERATOR", "ROLE_ADMIN")
    @PostMapping("/reviews")
    fun createReview(
        @PathVariable versionId: Long,
        @Validated @RequestBody reviewDto: ReviewDTO,
        principal: Principal
    ): ReviewView {
        val reviewer = userCredentialsService.getByEmail(principal.email)
        val review = reviewService.addReview(
            ReviewDTOToInfoAdapter(reviewDto, versionId, reviewer.id)
        )
        return mapper.createView(review)
    }

    @Secured("ROLE_MODERATOR", "ROLE_ADMIN")
    @PutMapping("/reviews/{reviewId}")
    fun updateReview(
        @PathVariable versionId: Long,
        @PathVariable reviewId: Long,
        @Validated @RequestBody dto: ReviewDTO,
        principal: Principal
    ): ReviewView {
        val user = userCredentialsService.getByEmail(principal.email)
        val review = reviewService.getById(reviewId, user)
        checkIdEquality(versionId, review.articleVersion.id)
        val updatedReview = reviewService.updateById(
            ReviewDTOToInfoAdapter(dto, reviewId, user.id),
            user
        )
        return mapper.createView(updatedReview)
    }

    @Secured("ROLE_MODERATOR", "ROLE_ADMIN")
    @DeleteMapping("/reviews/{reviewId}")
    fun deleteReview(
        @PathVariable versionId: Long,
        @PathVariable reviewId: Long,
        principal: Principal
    ) {
        val user = userCredentialsService.getByEmail(principal.email)
        val review = reviewService.getById(reviewId, user)
        checkIdEquality(versionId, review.articleVersion.id)
        reviewService.deleteReview(reviewId)
    }

    @PatchMapping("/{versionId}/request-review")
    fun requestReview(
        @PathVariable versionId: Long,
        principal: Principal
    ) {
        val user = userCredentialsService.getByEmail(principal.email)
        val version = articleVersionService.getById(versionId)
        if (version.canModify(user)) {
            throw AccessDeniedException("User has no access to this version")
        }
        // reviewService.requestReview(versionId)
    }

    @Secured("ROLE_MODERATOR", "ROLE_ADMIN")
    @PatchMapping("/{versionId}/approve")
    fun approve(
        @PathVariable versionId: Long,
        principal: Principal
    ) {
        // reviewService.approve(versionId)
    }

    @Secured("ROLE_MODERATOR", "ROLE_ADMIN")
    @PatchMapping("/{versionId}/approve-local")
    fun approveLocal(
        @PathVariable versionId: Long,
        principal: Principal
    ) {
        // reviewService.approveLocal(versionId)
    }

    @Secured("ROLE_MODERATOR", "ROLE_ADMIN")
    @PatchMapping("/{versionId}/request-changes")
    fun requestChanges(
        @PathVariable versionId: Long,
        @Validated @RequestBody reviewDto: ReviewDTO,
        principal: Principal
    ) {
        val reviewer = userCredentialsService.getByEmail(principal.email)
        val review = reviewService.addReview(
            ReviewDTOToInfoAdapter(reviewDto, versionId, reviewer.id)
        )
        // reviewService.requestChanges(review)
    }

    private fun checkIdEquality(versionId: Long, entityId: Long) {
        if (versionId != entityId) {
            throw ReviewNotFoundException("Review's article version id and request article version id doesn't match")
        }
    }
}
