package com.mabahmani.instasave.util

import android.util.Xml
import com.mabahmani.instasave.domain.model.MPD
import org.xmlpull.v1.XmlPullParser
import timber.log.Timber
import java.io.InputStream

object LiveStreamMPDXmlParser {

    fun parseMPD(inputStream: InputStream): MPD {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return MPD(readMPD(parser))
        }
    }

    private fun readMPD(parser: XmlPullParser): List<MPD.Period> {
        val periods = mutableListOf<MPD.Period>()

        parser.require(XmlPullParser.START_TAG, null, "MPD")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "Period") {
                periods.add(readPeriod(parser))
            } else {
                skip(parser)
            }
        }
        return periods
    }

    private fun readPeriod(parser: XmlPullParser): MPD.Period {
        parser.require(XmlPullParser.START_TAG, null, "Period")

        val adaptationSet: MutableList<MPD.AdaptationSet> = mutableListOf()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "AdaptationSet") {
                adaptationSet.add(readAdaptationSet(parser))
            } else {
                skip(parser)
            }

        }
        return MPD.Period(adaptationSet)
    }

    private fun readAdaptationSet(parser: XmlPullParser): MPD.AdaptationSet {

        parser.require(XmlPullParser.START_TAG, null, "AdaptationSet")

        val representation: MutableList<MPD.Representation> = mutableListOf()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "Representation") {
                representation.add(readRepresentation(parser))
            } else {
                skip(parser)
            }

        }

        representation.sortByDescending { it.width }

        return MPD.AdaptationSet(representation)
    }

    private fun readRepresentation(parser: XmlPullParser): MPD.Representation {

        parser.require(XmlPullParser.START_TAG, null, "Representation")

        val id = parser.getAttributeValue(null, "id")
        val mimeType = parser.getAttributeValue(null, "mimeType")
        val width = parser.getAttributeValue(null, "width")
        val height = parser.getAttributeValue(null, "height")

        val segmentTemplate: MutableList<MPD.SegmentTemplate> = mutableListOf()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "SegmentTemplate") {
                segmentTemplate.add(readSegmentTemplate(parser))
            } else {
                skip(parser)
            }

        }

        return MPD.Representation(id, mimeType, width, height, segmentTemplate)
    }

    private fun readSegmentTemplate(parser: XmlPullParser): MPD.SegmentTemplate {
        parser.require(XmlPullParser.START_TAG, null, "SegmentTemplate")

        val segmentTimeline: MutableList<MPD.SegmentTimeline> = mutableListOf()

        val initialization = parser.getAttributeValue(null, "initialization")
        val media = parser.getAttributeValue(null, "media")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "SegmentTimeline") {
                segmentTimeline.add(readSegmentTimeline(parser))
            } else {
                skip(parser)
            }

        }

        return MPD.SegmentTemplate(initialization, media, segmentTimeline)
    }

    private fun readSegmentTimeline(parser: XmlPullParser): MPD.SegmentTimeline {
        parser.require(XmlPullParser.START_TAG, null, "SegmentTimeline")

        val timelines: MutableList<MPD.S> = mutableListOf()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            Timber.d("readSegmentTimeline %s", parser.name)

            if (parser.name == "S") {
                timelines.add(readS(parser))
            } else {
                skip(parser)
            }

        }
        return MPD.SegmentTimeline(timelines)
    }

    private fun readS(parser: XmlPullParser): MPD.S {

        parser.require(XmlPullParser.START_TAG, null, "S")

        val t = parser.getAttributeValue(null, "t")
        val d = parser.getAttributeValue(null, "d")

        parser.next()

        return MPD.S(t, d)
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}