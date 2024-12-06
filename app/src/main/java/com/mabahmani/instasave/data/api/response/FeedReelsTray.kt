package com.mabahmani.instasave.data.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedReelsTray(
    @SerialName("broadcasts")
    val broadcasts: List<Broadcast?>? = null
) {
    @Serializable
    data class Broadcast(
        @SerialName("broadcast_owner")
        val broadcastOwner: BroadcastOwner? = null,
        @SerialName("cover_frame_url")
        val coverFrameUrl: String? = null,
        @SerialName("dash_abr_playback_url")
        val dashAbrPlaybackUrl: String? = null,
        @SerialName("id")
        val id: Long? = null,
        @SerialName("published_time")
        val publishedTime: Long? = null,
    ) {

        @Serializable
        data class BroadcastOwner(
            @SerialName("profile_pic_url")
            val profilePicUrl: String? = null,
            @SerialName("username")
            val username: String? = null
        )
    }
}