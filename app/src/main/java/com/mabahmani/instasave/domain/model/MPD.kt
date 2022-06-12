package com.mabahmani.instasave.domain.model

data class MPD(
    val periods:List<Period>
){
    data class Period(
        val adaptationSet: List<AdaptationSet>
    )

    data class AdaptationSet(
        val representation: List<Representation>
    )

    data class Representation(
        val id: String?,
        val mimeType: String?,
        val width: String?,
        val height: String?,
        val segmentTemplate: List<SegmentTemplate>
    )

    data class SegmentTemplate(
        val initialization: String?,
        val media: String?,
        val segmentTimeline: List<SegmentTimeline>
    )

    data class SegmentTimeline(
        val s: List<S>
    )

    data class S(
        val t: String?,
        val d: String?
    )
}
