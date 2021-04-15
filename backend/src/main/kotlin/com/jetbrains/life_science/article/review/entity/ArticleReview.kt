package com.jetbrains.life_science.article.review.entity

import com.jetbrains.life_science.user.entity.User
import com.jetbrains.life_science.article.version.entity.ArticleVersion
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class ArticleReview(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    var articleVersion: ArticleVersion,

    @Column(nullable = false)
    var comment: String,

    @ManyToOne
    var reviewer: User
)
