package com.mabahmani.instasave.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchTagRes(
    @SerialName("data")
    val data: Data?
) {
    @Serializable
    data class Data(
        @SerialName("recent")
        val recent: Recent?
    ) {
        @Serializable
        data class Recent(
            @SerialName("sections")
            val sections: List<Section?>?,
            @SerialName("next_max_id")
            val nextMaxId: String?
        ) {
            @Serializable
            data class Section(
                @SerialName("layout_content")
                val layoutContent: LayoutContent?,
                @SerialName("feed_type")
                val feedType: String?
            ) {
                @Serializable
                data class LayoutContent(
                    @SerialName("medias")
                    val medias: List<Media?>?
                ) {
                    @Serializable
                    data class Media(
                        @SerialName("media")
                        val item: Item?
                    ) {
                        @Serializable
                        data class Item(
                            @SerialName("pk")
                            val pk: String?,
                            @SerialName("media_type")
                            val mediaType: Int?,
                            @SerialName("product_type")
                            val productType: String?,
                            @SerialName("user")
                            val user: User?,
                        ) {
                            @Serializable
                            data class User(
                                @SerialName("pk")
                                val pk: String?,
                                @SerialName("username")
                                val username: String?,
                                @SerialName("full_name")
                                val fullName: String?,
                                @SerialName("profile_pic_url")
                                val profilePicUrl: String?,
                            )
                        }
                    }
                }
            }
        }
    }
}

