package com.mabahmani.instasave.data.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedReelsTray(
    @SerialName("broadcasts")
    val broadcasts: List<Broadcast?>? = null,
    @SerialName("face_filter_nux_version")
    val faceFilterNuxVersion: Long? = null,
    @SerialName("has_new_nux_story")
    val hasNewNuxStory: Boolean? = null,
    @SerialName("nudge_story_reaction_ufi")
    val nudgeStoryReactionUfi: Boolean? = null,
    @SerialName("refresh_window_ms")
    val refreshWindowMs: Long? = null,
    @SerialName("response_timestamp")
    val responseTimestamp: Long? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("sticker_version")
    val stickerVersion: Long? = null,
    @SerialName("stories_viewer_gestures_nux_eligible")
    val storiesViewerGesturesNuxEligible: Boolean? = null,
    @SerialName("story_likes_config")
    val storyLikesConfig: StoryLikesConfig? = null,
    @SerialName("story_ranking_token")
    val storyRankingToken: String? = null,
    @SerialName("tray")
    val tray: List<Tray?>? = null
) {
    @Serializable
    data class Broadcast(
        @SerialName("broadcast_experiments")
        val broadcastExperiments: BroadcastExperiments? = null,
        @SerialName("broadcast_message")
        val broadcastMessage: String? = null,
        @SerialName("broadcast_owner")
        val broadcastOwner: BroadcastOwner? = null,
        @SerialName("broadcast_status")
        val broadcastStatus: String? = null,
        @SerialName("cobroadcasters")
        val cobroadcasters: List<Cobroadcaster?>? = null,
        @SerialName("cover_frame_url")
        val coverFrameUrl: String? = null,
        @SerialName("dash_abr_playback_url")
        val dashAbrPlaybackUrl: String? = null,
        @SerialName("dash_playback_url")
        val dashPlaybackUrl: String? = null,
        @SerialName("dimensions")
        val dimensions: Dimensions? = null,
        @SerialName("hide_from_feed_unit")
        val hideFromFeedUnit: Boolean? = null,
        @SerialName("id")
        val id: Long? = null,
        @SerialName("internal_only")
        val internalOnly: Boolean? = null,
        @SerialName("is_gaming_content")
        val isGamingContent: Boolean? = null,
        @SerialName("is_live_comment_mention_enabled")
        val isLiveCommentMentionEnabled: Boolean? = null,
        @SerialName("is_live_comment_replies_enabled")
        val isLiveCommentRepliesEnabled: Boolean? = null,
        @SerialName("is_player_live_trace_enabled")
        val isPlayerLiveTraceEnabled: Long? = null,
        @SerialName("is_viewer_comment_allowed")
        val isViewerCommentAllowed: Boolean? = null,
        @SerialName("live_post_id")
        val livePostId: Long? = null,
        @SerialName("media_id")
        val mediaId: String? = null,
        @SerialName("muted")
        val muted: Boolean? = null,
        @SerialName("organic_tracking_token")
        val organicTrackingToken: String? = null,
        @SerialName("published_time")
        val publishedTime: Long? = null,
        @SerialName("ranked_position")
        val rankedPosition: Long? = null,
        @SerialName("seen_ranked_position")
        val seenRankedPosition: Long? = null,
        @SerialName("video_duration")
        val videoDuration: Double? = null,
        @SerialName("viewer_count")
        val viewerCount: Double? = null,
        @SerialName("visibility")
        val visibility: Long? = null
    ) {
        @Serializable
        data class BroadcastExperiments(
            @SerialName("ig_allow_4p_live_with")
            val igAllow4pLiveWith: IgAllow4pLiveWith? = null,
            @SerialName("ig_live_audio_video_toggle")
            val igLiveAudioVideoToggle: IgLiveAudioVideoToggle? = null,
            @SerialName("ig_live_badges_ufi")
            val igLiveBadgesUfi: IgLiveBadgesUfi? = null,
            @SerialName("ig_live_comment_interactions")
            val igLiveCommentInteractions: IgLiveCommentInteractions? = null,
            @SerialName("ig_live_comment_subscription")
            val igLiveCommentSubscription: IgLiveCommentSubscription? = null,
            @SerialName("ig_live_emoji_reactions")
            val igLiveEmojiReactions: IgLiveEmojiReactions? = null,
            @SerialName("ig_live_halo_call_controls")
            val igLiveHaloCallControls: IgLiveHaloCallControls? = null,
            @SerialName("ig_live_share_system_comment")
            val igLiveShareSystemComment: IgLiveShareSystemComment? = null,
            @SerialName("ig_live_upvoteable_qa")
            val igLiveUpvoteableQa: IgLiveUpvoteableQa? = null,
            @SerialName("ig_live_use_rsys_rtc_infra")
            val igLiveUseRsysRtcInfra: IgLiveUseRsysRtcInfra? = null,
            @SerialName("ig_live_viewer_redesign_broadcaster_v1")
            val igLiveViewerRedesignBroadcasterV1: IgLiveViewerRedesignBroadcasterV1? = null,
            @SerialName("ig_live_viewer_to_viewer_waves")
            val igLiveViewerToViewerWaves: IgLiveViewerToViewerWaves? = null
        ) {
            @Serializable
            data class IgAllow4pLiveWith(
                @SerialName("allow")
                val allow: Boolean? = null
            )

            @Serializable
            data class IgLiveAudioVideoToggle(
                @SerialName("audio_toggle_enabled")
                val audioToggleEnabled: Boolean? = null,
                @SerialName("video_toggle_enabled")
                val videoToggleEnabled: Boolean? = null
            )

            @Serializable
            data class IgLiveBadgesUfi(
                @SerialName("badges_always_on_enabled")
                val badgesAlwaysOnEnabled: Boolean? = null
            )

            @Serializable
            data class IgLiveCommentInteractions(
                @SerialName("android_is_required_mvvm_enabled")
                val androidIsRequiredMvvmEnabled: Boolean? = null,
                @SerialName("is_broadcast_level_expand_enabled")
                val isBroadcastLevelExpandEnabled: Boolean? = null,
                @SerialName("is_host_comment_liking_enabled")
                val isHostCommentLikingEnabled: Boolean? = null,
                @SerialName("is_host_comment_reply_redesign_enabled")
                val isHostCommentReplyRedesignEnabled: Boolean? = null
            )

            @Serializable
            data class IgLiveCommentSubscription(
                @SerialName("is_broadcast_enabled")
                val isBroadcastEnabled: Boolean? = null
            )

            @Serializable
            data class IgLiveEmojiReactions(
                @SerialName("is_host_enabled")
                val isHostEnabled: Boolean? = null,
                @SerialName("use_emoji_set_2")
                val useEmojiSet2: Boolean? = null
            )

            @Serializable
            data class IgLiveHaloCallControls(
                @SerialName("tap_state_animation_enabled")
                val tapStateAnimationEnabled: Boolean? = null,
                @SerialName("tap_state_bottom_call_controls_enabled")
                val tapStateBottomCallControlsEnabled: Boolean? = null,
                @SerialName("tap_to_show_pill_enabled")
                val tapToShowPillEnabled: Boolean? = null
            )

            @Serializable
            data class IgLiveShareSystemComment(
                @SerialName("join_request_system_comment_delay_5_else_0")
                val joinRequestSystemCommentDelay5Else0: Boolean? = null,
                @SerialName("share_system_comment_delay_10_else_5")
                val shareSystemCommentDelay10Else5: Boolean? = null,
                @SerialName("show_join_request_system_comment")
                val showJoinRequestSystemComment: Boolean? = null,
                @SerialName("show_share_system_comment")
                val showShareSystemComment: Boolean? = null
            )

            @Serializable
            data class IgLiveUpvoteableQa(
                @SerialName("enabled")
                val enabled: Boolean? = null
            )

            @Serializable
            data class IgLiveUseRsysRtcInfra(
                @SerialName("enabled")
                val enabled: Boolean? = null
            )

            @Serializable
            data class IgLiveViewerRedesignBroadcasterV1(
                @SerialName("aspect_ratio_change_enabled")
                val aspectRatioChangeEnabled: Boolean? = null,
                @SerialName("comment_redesign_combined_test_enabled")
                val commentRedesignCombinedTestEnabled: Boolean? = null,
                @SerialName("is_aspect_ratio_16_9")
                val isAspectRatio169: Boolean? = null,
                @SerialName("viewer_redesign_combined_test_enabled")
                val viewerRedesignCombinedTestEnabled: Boolean? = null,
                @SerialName("viewer_redesign_v2_combined_test_enabled")
                val viewerRedesignV2CombinedTestEnabled: Boolean? = null
            )

            @Serializable
            data class IgLiveViewerToViewerWaves(
                @SerialName("enabled")
                val enabled: Boolean? = null
            )
        }

        @Serializable
        data class BroadcastOwner(
            @SerialName("follow_friction_type")
            val followFrictionType: Long? = null,
            @SerialName("friendship_status")
            val friendshipStatus: FriendshipStatus? = null,
            @SerialName("full_name")
            val fullName: String? = null,
            @SerialName("growth_friction_info")
            val growthFrictionInfo: GrowthFrictionInfo? = null,
            @SerialName("interop_messaging_user_fbid")
            val interopMessagingUserFbid: Long? = null,
            @SerialName("is_private")
            val isPrivate: Boolean? = null,
            @SerialName("is_verified")
            val isVerified: Boolean? = null,
            @SerialName("pk")
            val pk: Long? = null,
            @SerialName("profile_pic_id")
            val profilePicId: String? = null,
            @SerialName("profile_pic_url")
            val profilePicUrl: String? = null,
            @SerialName("username")
            val username: String? = null
        ) {
            @Serializable
            data class FriendshipStatus(
                @SerialName("blocking")
                val blocking: Boolean? = null,
                @SerialName("followed_by")
                val followedBy: Boolean? = null,
                @SerialName("following")
                val following: Boolean? = null,
                @SerialName("incoming_request")
                val incomingRequest: Boolean? = null,
                @SerialName("is_bestie")
                val isBestie: Boolean? = null,
                @SerialName("is_feed_favorite")
                val isFeedFavorite: Boolean? = null,
                @SerialName("is_private")
                val isPrivate: Boolean? = null,
                @SerialName("is_restricted")
                val isRestricted: Boolean? = null,
                @SerialName("muting")
                val muting: Boolean? = null,
                @SerialName("outgoing_request")
                val outgoingRequest: Boolean? = null
            )

            @Serializable
            data class GrowthFrictionInfo(
                @SerialName("has_active_interventions")
                val hasActiveInterventions: Boolean? = null,
                @SerialName("interventions")
                val interventions: Interventions? = null
            ) {
                @Serializable
                class Interventions
            }
        }

        @Serializable
        data class Cobroadcaster(
            @SerialName("follow_friction_type")
            val followFrictionType: Long? = null,
            @SerialName("friendship_status")
            val friendshipStatus: FriendshipStatus? = null,
            @SerialName("full_name")
            val fullName: String? = null,
            @SerialName("growth_friction_info")
            val growthFrictionInfo: GrowthFrictionInfo? = null,
            @SerialName("is_private")
            val isPrivate: Boolean? = null,
            @SerialName("is_verified")
            val isVerified: Boolean? = null,
            @SerialName("pk")
            val pk: Long? = null,
            @SerialName("profile_pic_id")
            val profilePicId: String? = null,
            @SerialName("profile_pic_url")
            val profilePicUrl: String? = null,
            @SerialName("username")
            val username: String? = null
        ) {
            @Serializable
            data class FriendshipStatus(
                @SerialName("followed_by")
                val followedBy: Boolean? = null,
                @SerialName("following")
                val following: Boolean? = null,
                @SerialName("incoming_request")
                val incomingRequest: Boolean? = null,
                @SerialName("is_bestie")
                val isBestie: Boolean? = null,
                @SerialName("is_feed_favorite")
                val isFeedFavorite: Boolean? = null,
                @SerialName("is_private")
                val isPrivate: Boolean? = null,
                @SerialName("is_restricted")
                val isRestricted: Boolean? = null,
                @SerialName("outgoing_request")
                val outgoingRequest: Boolean? = null
            )

            @Serializable
            data class GrowthFrictionInfo(
                @SerialName("has_active_interventions")
                val hasActiveInterventions: Boolean? = null,
                @SerialName("interventions")
                val interventions: Interventions? = null
            ) {
                @Serializable
                class Interventions
            }
        }

        @Serializable
        data class Dimensions(
            @SerialName("height")
            val height: Long? = null,
            @SerialName("width")
            val width: Long? = null
        )
    }

    @Serializable
    data class StoryLikesConfig(
        @SerialName("is_enabled")
        val isEnabled: Boolean? = null,
        @SerialName("ufi_type")
        val ufiType: Long? = null
    )

    @Serializable
    data class Tray(
        @SerialName("can_gif_quick_reply")
        val canGifQuickReply: Boolean? = null,
        @SerialName("can_react_with_avatar")
        val canReactWithAvatar: Boolean? = null,
        @SerialName("can_reply")
        val canReply: Boolean? = null,
        @SerialName("can_reshare")
        val canReshare: Boolean? = null,
        @SerialName("expiring_at")
        val expiringAt: Long? = null,
        @SerialName("has_besties_media")
        val hasBestiesMedia: Boolean? = null,
        @SerialName("has_fan_club_media")
        val hasFanClubMedia: Boolean? = null,
        @SerialName("has_pride_media")
        val hasPrideMedia: Boolean? = null,
        @SerialName("has_video")
        val hasVideo: Boolean? = null,
        @SerialName("id")
        val id: Long? = null,
        @SerialName("is_cacheable")
        val isCacheable: Boolean? = null,
        @SerialName("items")
        val items: List<Item?>? = null,
        @SerialName("latest_besties_reel_media")
        val latestBestiesReelMedia: Double? = null,
        @SerialName("latest_reel_media")
        val latestReelMedia: Long? = null,
        @SerialName("media_count")
        val mediaCount: Long? = null,
        @SerialName("muted")
        val muted: Boolean? = null,
        @SerialName("prefetch_count")
        val prefetchCount: Long? = null,
        @SerialName("ranked_position")
        val rankedPosition: Long? = null,
        @SerialName("ranker_scores")
        val rankerScores: RankerScores? = null,
        @SerialName("reel_type")
        val reelType: String? = null,
        @SerialName("seen")
        val seen: Long? = null,
        @SerialName("seen_ranked_position")
        val seenRankedPosition: Long? = null,
        @SerialName("story_duration_secs")
        val storyDurationSecs: Long? = null,
        @SerialName("user")
        val user: User? = null
    ) {
        @Serializable
        data class Item(
            @SerialName("can_reply")
            val canReply: Boolean? = null,
            @SerialName("can_reshare")
            val canReshare: Boolean? = null,
            @SerialName("can_see_insights_as_brand")
            val canSeeInsightsAsBrand: Boolean? = null,
            @SerialName("can_send_custom_emojis")
            val canSendCustomEmojis: Boolean? = null,
            @SerialName("can_viewer_save")
            val canViewerSave: Boolean? = null,
            @SerialName("caption_is_edited")
            val captionIsEdited: Boolean? = null,
            @SerialName("caption_position")
            val captionPosition: Double? = null,
            @SerialName("client_cache_key")
            val clientCacheKey: String? = null,
            @SerialName("code")
            val code: String? = null,
            @SerialName("comment_inform_treatment")
            val commentInformTreatment: CommentInformTreatment? = null,
            @SerialName("commerciality_status")
            val commercialityStatus: String? = null,
            @SerialName("creative_config")
            val creativeConfig: CreativeConfig? = null,
            @SerialName("deleted_reason")
            val deletedReason: Long? = null,
            @SerialName("device_timestamp")
            val deviceTimestamp: Long? = null,
            @SerialName("expiring_at")
            val expiringAt: Long? = null,
            @SerialName("filter_type")
            val filterType: Long? = null,
            @SerialName("has_audio")
            val hasAudio: Boolean? = null,
            @SerialName("has_liked")
            val hasLiked: Boolean? = null,
            @SerialName("has_shared_to_fb")
            val hasSharedToFb: Long? = null,
            @SerialName("id")
            val id: String? = null,
            @SerialName("image_versions")
            val imageVersions: List<ImageVersion?>? = null,
            @SerialName("imported_taken_at")
            val importedTakenAt: Long? = null,
            @SerialName("integrity_review_decision")
            val integrityReviewDecision: String? = null,
            @SerialName("is_in_profile_grid")
            val isInProfileGrid: Boolean? = null,
            @SerialName("is_non_terminal_video_segment")
            val isNonTerminalVideoSegment: Boolean? = null,
            @SerialName("is_organic_product_tagging_eligible")
            val isOrganicProductTaggingEligible: Boolean? = null,
            @SerialName("is_paid_partnership")
            val isPaidPartnership: Boolean? = null,
            @SerialName("is_reel_media")
            val isReelMedia: Boolean? = null,
            @SerialName("is_terminal_video_segment")
            val isTerminalVideoSegment: Boolean? = null,
            @SerialName("is_unified_video")
            val isUnifiedVideo: Boolean? = null,
            @SerialName("is_visual_reply_commenter_notice_enabled")
            val isVisualReplyCommenterNoticeEnabled: Boolean? = null,
            @SerialName("like_and_view_counts_disabled")
            val likeAndViewCountsDisabled: Boolean? = null,
            @SerialName("media_type")
            val mediaType: Long? = null,
            @SerialName("organic_tracking_token")
            val organicTrackingToken: String? = null,
            @SerialName("original_media_has_visual_reply_media")
            val originalMediaHasVisualReplyMedia: Boolean? = null,
            @SerialName("photo_of_you")
            val photoOfYou: Boolean? = null,
            @SerialName("pk")
            val pk: Long? = null,
            @SerialName("product_type")
            val productType: String? = null,
            @SerialName("profile_grid_control_enabled")
            val profileGridControlEnabled: Boolean? = null,
            @SerialName("reel_mentions")
            val reelMentions: List<ReelMention?>? = null,
            @SerialName("segmented_video_group_id")
            val segmentedVideoGroupId: String? = null,
            @SerialName("sharing_friction_info")
            val sharingFrictionInfo: SharingFrictionInfo? = null,
            @SerialName("show_one_tap_fb_share_tooltip")
            val showOneTapFbShareTooltip: Boolean? = null,
            @SerialName("supports_reel_reactions")
            val supportsReelReactions: Boolean? = null,
            @SerialName("taken_at")
            val takenAt: Long? = null,
            @SerialName("user")
            val user: User? = null,
            @SerialName("video_duration")
            val videoDuration: Double? = null,
            @SerialName("video_versions")
            val videoVersions: List<VideoVersion?>? = null
        ) {
            @Serializable
            data class CommentInformTreatment(
                @SerialName("should_have_inform_treatment")
                val shouldHaveInformTreatment: Boolean? = null,
                @SerialName("text")
                val text: String? = null,
            )

            @Serializable
            data class CreativeConfig(
                @SerialName("attribution_id")
                val attributionId: Long? = null,
                @SerialName("attribution_user")
                val attributionUser: AttributionUser? = null,
                @SerialName("attribution_username")
                val attributionUsername: String? = null,
                @SerialName("camera_facing")
                val cameraFacing: String? = null,
                @SerialName("capture_type")
                val captureType: String? = null,
                @SerialName("effect_action_sheet")
                val effectActionSheet: EffectActionSheet? = null,
                @SerialName("effect_actions")
                val effectActions: List<String?>? = null,
                @SerialName("effect_configs")
                val effectConfigs: List<EffectConfig?>? = null,
                @SerialName("effect_id")
                val effectId: Long? = null,
                @SerialName("effect_ids")
                val effectIds: List<Long?>? = null,
                @SerialName("effect_name")
                val effectName: String? = null,
                @SerialName("effect_preview")
                val effectPreview: EffectPreview? = null,
                @SerialName("face_effect_id")
                val faceEffectId: Long? = null,
                @SerialName("is_spot_effect")
                val isSpotEffect: Boolean? = null,
                @SerialName("is_spot_recognition_effect")
                val isSpotRecognitionEffect: Boolean? = null,
                @SerialName("name")
                val name: String? = null,
                @SerialName("persisted_effect_metadata_json")
                val persistedEffectMetadataJson: String? = null,
                @SerialName("save_status")
                val saveStatus: String? = null,
                @SerialName("should_render_try_it_on")
                val shouldRenderTryItOn: Boolean? = null
            ) {
                @Serializable
                data class AttributionUser(
                    @SerialName("instagram_user_id")
                    val instagramUserId: String? = null,
                    @SerialName("profile_picture")
                    val profilePicture: ProfilePicture? = null,
                    @SerialName("username")
                    val username: String? = null
                ) {
                    @Serializable
                    data class ProfilePicture(
                        @SerialName("uri")
                        val uri: String? = null
                    )
                }

                @Serializable
                data class EffectActionSheet(
                    @SerialName("primary_actions")
                    val primaryActions: List<String?>? = null,
                    @SerialName("secondary_actions")
                    val secondaryActions: List<String?>? = null
                )

                @Serializable
                data class EffectConfig(
                    @SerialName("attribution_user")
                    val attributionUser: AttributionUser? = null,
                    @SerialName("attribution_user_id")
                    val attributionUserId: String? = null,
                    @SerialName("effect_action_sheet")
                    val effectActionSheet: EffectActionSheet? = null,
                    @SerialName("effect_actions")
                    val effectActions: List<String?>? = null,
                    @SerialName("failure_code")
                    val failureCode: String? = null,
                    @SerialName("failure_reason")
                    val failureReason: String? = null,
                    @SerialName("id")
                    val id: String? = null,
                    @SerialName("is_creative_tool")
                    val isCreativeTool: Boolean? = null,
                    @SerialName("is_spot_effect")
                    val isSpotEffect: Boolean? = null,
                    @SerialName("is_spot_recognition_effect")
                    val isSpotRecognitionEffect: Boolean? = null,
                    @SerialName("name")
                    val name: String? = null,
                    @SerialName("save_status")
                    val saveStatus: String? = null,
                    @SerialName("thumbnail_image")
                    val thumbnailImage: ThumbnailImage? = null
                ) {
                    @Serializable
                    data class AttributionUser(
                        @SerialName("instagram_user_id")
                        val instagramUserId: String? = null,
                        @SerialName("profile_picture")
                        val profilePicture: ProfilePicture? = null,
                        @SerialName("username")
                        val username: String? = null
                    ) {
                        @Serializable
                        data class ProfilePicture(
                            @SerialName("uri")
                            val uri: String? = null
                        )
                    }

                    @Serializable
                    data class EffectActionSheet(
                        @SerialName("primary_actions")
                        val primaryActions: List<String?>? = null,
                        @SerialName("secondary_actions")
                        val secondaryActions: List<String?>? = null
                    )

                    @Serializable
                    data class ThumbnailImage(
                        @SerialName("uri")
                        val uri: String? = null
                    )
                }

                @Serializable
                data class EffectPreview(
                    @SerialName("thumbnail_image")
                    val thumbnailImage: ThumbnailImage? = null
                ) {
                    @Serializable
                    data class ThumbnailImage(
                        @SerialName("uri")
                        val uri: String? = null
                    )
                }
            }

            @Serializable
            data class ImageVersion(
                @SerialName("height")
                val height: Long? = null,
                @SerialName("type")
                val type: Long? = null,
                @SerialName("url")
                val url: String? = null,
                @SerialName("width")
                val width: Long? = null
            )

            @Serializable
            data class ReelMention(
                @SerialName("display_type")
                val displayType: String? = null,
                @SerialName("height")
                val height: Double? = null,
                @SerialName("is_fb_sticker")
                val isFbSticker: Long? = null,
                @SerialName("is_hidden")
                val isHidden: Long? = null,
                @SerialName("is_pinned")
                val isPinned: Long? = null,
                @SerialName("is_sticker")
                val isSticker: Long? = null,
                @SerialName("rotation")
                val rotation: Double? = null,
                @SerialName("user")
                val user: User? = null,
                @SerialName("width")
                val width: Double? = null,
                @SerialName("x")
                val x: Double? = null,
                @SerialName("y")
                val y: Double? = null,
                @SerialName("z")
                val z: Long? = null
            ) {
                @Serializable
                data class User(
                    @SerialName("follow_friction_type")
                    val followFrictionType: Long? = null,
                    @SerialName("friendship_status")
                    val friendshipStatus: FriendshipStatus? = null,
                    @SerialName("full_name")
                    val fullName: String? = null,
                    @SerialName("growth_friction_info")
                    val growthFrictionInfo: GrowthFrictionInfo? = null,
                    @SerialName("is_private")
                    val isPrivate: Boolean? = null,
                    @SerialName("is_verified")
                    val isVerified: Boolean? = null,
                    @SerialName("pk")
                    val pk: Long? = null,
                    @SerialName("profile_pic_id")
                    val profilePicId: String? = null,
                    @SerialName("profile_pic_url")
                    val profilePicUrl: String? = null,
                    @SerialName("username")
                    val username: String? = null
                ) {
                    @Serializable
                    data class FriendshipStatus(
                        @SerialName("following")
                        val following: Boolean? = null,
                        @SerialName("is_bestie")
                        val isBestie: Boolean? = null,
                        @SerialName("is_muting_reel")
                        val isMutingReel: Boolean? = null,
                        @SerialName("muting")
                        val muting: Boolean? = null,
                        @SerialName("outgoing_request")
                        val outgoingRequest: Boolean? = null
                    )

                    @Serializable
                    data class GrowthFrictionInfo(
                        @SerialName("has_active_interventions")
                        val hasActiveInterventions: Boolean? = null,
                        @SerialName("interventions")
                        val interventions: Interventions? = null
                    ) {
                        @Serializable
                        class Interventions
                    }
                }
            }

            @Serializable
            data class SharingFrictionInfo(
                @SerialName("should_have_sharing_friction")
                val shouldHaveSharingFriction: Boolean? = null
            )

            @Serializable
            data class User(
                @SerialName("is_private")
                val isPrivate: Boolean? = null,
                @SerialName("pk")
                val pk: Long? = null
            )

            @Serializable
            data class VideoVersion(
                @SerialName("height")
                val height: Long? = null,
                @SerialName("id")
                val id: String? = null,
                @SerialName("type")
                val type: Long? = null,
                @SerialName("url")
                val url: String? = null,
                @SerialName("width")
                val width: Long? = null
            )
        }

        @Serializable
        data class RankerScores(
            @SerialName("fp")
            val fp: Double? = null,
            @SerialName("ptap")
            val ptap: Double? = null,
            @SerialName("vm")
            val vm: Double? = null
        )

        @Serializable
        data class User(
            @SerialName("follow_friction_type")
            val followFrictionType: Long? = null,
            @SerialName("friendship_status")
            val friendshipStatus: FriendshipStatus? = null,
            @SerialName("full_name")
            val fullName: String? = null,
            @SerialName("growth_friction_info")
            val growthFrictionInfo: GrowthFrictionInfo? = null,
            @SerialName("is_private")
            val isPrivate: Boolean? = null,
            @SerialName("is_verified")
            val isVerified: Boolean? = null,
            @SerialName("pk")
            val pk: Long? = null,
            @SerialName("profile_pic_id")
            val profilePicId: String? = null,
            @SerialName("profile_pic_url")
            val profilePicUrl: String? = null,
            @SerialName("username")
            val username: String? = null
        ) {
            @Serializable
            data class FriendshipStatus(
                @SerialName("following")
                val following: Boolean? = null,
                @SerialName("is_bestie")
                val isBestie: Boolean? = null,
                @SerialName("is_muting_reel")
                val isMutingReel: Boolean? = null,
                @SerialName("muting")
                val muting: Boolean? = null,
                @SerialName("outgoing_request")
                val outgoingRequest: Boolean? = null
            )

            @Serializable
            data class GrowthFrictionInfo(
                @SerialName("has_active_interventions")
                val hasActiveInterventions: Boolean? = null,
                @SerialName("interventions")
                val interventions: Interventions? = null
            ) {
                @Serializable
                class Interventions
            }
        }
    }
}