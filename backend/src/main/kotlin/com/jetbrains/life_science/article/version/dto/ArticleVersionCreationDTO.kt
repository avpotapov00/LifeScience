package com.jetbrains.life_science.article.version.dto

import com.jetbrains.life_science.article.section.dto.SectionInnerDTO
import javax.validation.constraints.NotBlank

data class ArticleVersionCreationDTO(

    @field:NotBlank
    val name: String,

    val sections: List<SectionInnerDTO> = emptyList()

)
