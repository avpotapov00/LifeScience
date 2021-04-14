package com.jetbrains.life_science.article.review.factory

import com.jetbrains.life_science.article.review.entity.ArticleReview
import com.jetbrains.life_science.article.review.service.ArticleReviewInfo
import com.jetbrains.life_science.user.entity.User
import com.jetbrains.life_science.article.version.entity.ArticleVersion
import org.springframework.stereotype.Component

@Component
class ArticleReviewFactory {
    fun create(info: ArticleReviewInfo, articleVersion: ArticleVersion, user: User): ArticleReview {
        return ArticleReview(0, articleVersion, info.comment, user)
    }
}
