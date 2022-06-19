package com.mabahmani.instasave.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Media(
    @SerialName("items")
    val items: List<Item>?
) {
    @Serializable
    data class Item(
        @SerialName("pk")
        val id: String? = null,
        @SerialName("code")
        val code: String? = null,
        @SerialName("media_type")
        val mediaType: Int? = null,
        @SerialName("image_versions2")
        val image: Image? = null,
        @SerialName("user")
        val user: User? = null,
        @SerialName("video_versions")
        val videos: List<Video>? = null,
        @SerialName("product_type")
        val productType: String? = null,
        @SerialName("carousel_media_count")
        val slidesCount: Int? = null,
        @SerialName("carousel_media")
        val slides: List<Item>? = null,
    ) {
        @Serializable
        data class Image(
            @SerialName("candidates")
            val candidates: List<Candidate>? = null
        ) {
            @Serializable
            data class Candidate(
                @SerialName("width")
                val width: Int? = null,
                @SerialName("height")
                val height: Int? = null,
                @SerialName("url")
                val url: String? = null
            )
        }

        @Serializable
        data class User(
            val username: String? = null
        )

        @Serializable
        data class Video(
            @SerialName("type")
            val type: Int? = null,
            @SerialName("width")
            val width: Int? = null,
            @SerialName("height")
            val height: Int? = null,
            @SerialName("url")
            val url: String? = null,
            @SerialName("id")
            val id: String? = null
        )

        fun getBestVideo(): Video?{
            return videos?.sortedByDescending { it.width }?.get(0)
        }

        fun getBestImage(): Image.Candidate?{
            return image?.candidates?.sortedByDescending { it.width }?.get(0)
        }
        fun getLowImage(): Image.Candidate?{
            return image?.candidates?.sortedBy { it.width }?.get(0)
        }
    }


}
