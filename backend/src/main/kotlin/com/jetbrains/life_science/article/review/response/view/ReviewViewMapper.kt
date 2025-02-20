package com.jetbrains.life_science.article.review.response.view

import com.jetbrains.life_science.article.review.response.entity.Review
import org.springframework.stereotype.Component

@Component
class ReviewViewMapper {
    fun toView(review: Review): ReviewView {
        return ReviewView(
            id = review.id,
            reviewRequestId = review.reviewRequest.id,
            comment = review.comment,
            reviewerId = review.reviewer.id
        )
    }

    fun toViews(reviews: List<Review>): List<ReviewView> {
        return reviews.map { toView(it) }
    }
}
