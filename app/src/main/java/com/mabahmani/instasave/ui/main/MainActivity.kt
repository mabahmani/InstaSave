package com.mabahmani.instasave.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Xml
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mabahmani.instasave.service.DownloadLiveStreamsService
import com.mabahmani.instasave.ui.main.download.DownloadScreen
import com.mabahmani.instasave.ui.main.home.HomeScreen
import com.mabahmani.instasave.ui.main.livestream.LiveStreamScreen
import com.mabahmani.instasave.ui.main.search.SearchScreen
import com.mabahmani.instasave.ui.main.story.StoryScreen
import com.mabahmani.instasave.ui.theme.InstaSaveTheme
import com.mabahmani.instasave.util.AppConstants
import com.mabahmani.instasave.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import timber.log.Timber
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var input: InputStream? = null
    var output: OutputStream? = null
    var connection: HttpURLConnection? = null

    val videoUrlList = mutableListOf<String?>()
    val audioUrlList = mutableListOf<String?>()

    var audioInitDownloaded = false
    var videoInitDownloaded = false

    var lastVideoTimeStamp = 0L
    var lastAudioTimeStamp = 0L

    var downloadInProgress = true

    var refreshDuplicateCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        val file = File(filesDir, "downloads/out.mp4")
//        file.mkdir()
//        val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val outputFile = File(downloadFolder, "out.mp4")
//
//        val ffmpeg = FFmpegKit.executeAsync(
//            "-i https://scontent-frt3-1.cdninstagram.com/hvideo-odn-cln/_nc_cat-108/v/rgcr334GcFdzJTwj2-ZeH/_nc_ohc-BvAkjMCIoEQAX_wgYZ3/live-dash/dash-hd/17941458107009736.mpd?ccb=2-4&ms=m_CN&oh=00_AT-Pf0rCan2MPcEV_H5F5TVw2gIc9anlt4rgW-73fYdBxg&oe=6286F2B5 -c copy -bsf:a aac_adtstoasc ${outputFile.path}",
//            { Timber.d("FFmpegKit %s", it) },
//            { Timber.d("FFmpegKit %s", it) },
//            { Timber.d("FFmpegKit %s", it) }
//        )

//        FFmpegKit.executeAsync(
//            "-y -i https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4 ${File(downloadFolder, "out1.mp4").path}"){
//
//            Timber.d("FFmpegKit %s", it)
//        }
//
//        FFmpegKit.executeAsync(
//            "-y -i https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4 ${File(downloadFolder, "out2.mp4").path}"){
//            Timber.d("FFmpegKit %s", it)
//        }

//        FFmpegKitConfig.enableFFmpegSessionCompleteCallback {
//            Timber.d("FFmpegKitConfig %s", it)
//        }

//
//        GlobalScope.launch {
//            delay(60000)
//            ffmpeg.cancel()
//        }

//        startService(Intent(this, DownloadLiveStreamsService::class.java).apply {
//            putExtra(AppConstants.Args.ID, 5L)
//            putExtra(AppConstants.Args.USERNAME, "asadi")
//            putExtra(AppConstants.Args.URL, "https://scontent-frt3-1.cdninstagram.com/hvideo-odn-cln/_nc_cat-108/v/rGLM335_dkGOMyDar4Mbx/_nc_ohc-nR50ULzyBl8AX_utAfu/live-dash/dash-abr/17928288125163274.mpd?ccb=2-4&ms=m_CN&oh=00_AT8QBNOnFkQqE5es3fS5GEIwY_kKQFiX514-VwDbES6clg&oe=62983763")
//        })
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            startService(Intent(this, DownloadLiveStreamsService::class.java).apply {
//                action = NotificationHelper.STOP_RECORDING_LIVE
//                putExtra(AppConstants.Args.ID, 5L)
//                putExtra(AppConstants.Args.USERNAME, "asadi")
//                putExtra(AppConstants.Args.URL, "https://scontent-frt3-1.cdninstagram.com/hvideo-odn-cln/_nc_cat-108/v/rGLM335_dkGOMyDar4Mbx/_nc_ohc-nR50ULzyBl8AX_utAfu/live-dash/dash-abr/17928288125163274.mpd?ccb=2-4&ms=m_CN&oh=00_AT8QBNOnFkQqE5es3fS5GEIwY_kKQFiX514-VwDbES6clg&oe=62983763")
//            })
//        },5000
//
//        )


//        startService(Intent(this, DownloadLiveStreamsService::class.java).apply {
//            putExtra(AppConstants.Args.ID, 6L)
//            putExtra(AppConstants.Args.USERNAME, "amin_user6")
//            putExtra(AppConstants.Args.URL, "https://scontent-frt3-1.cdninstagram.com/hvideo-cln-odn/_nc_cat-108/v/rfg2VjrrxAou9NW05YWZY/_nc_ohc-3WXMFnQllZoAX-6S8sv/live-dash/dash-abr/17921601017390400.mpd?ccb=2-4&ms=m_CN&oh=00_AT9aRDWOSKAJl7ueKhdsuJbdPNXUCcg6DkOOd_WFu_qC7g&oe=62928C2D")
//        })




//        val mainHandler = Handler(Looper.getMainLooper())
//
//        mainHandler.post(object : Runnable {
//            override fun run() {
//                if (refreshDuplicateCount < 50){
//                    downloadMpd()
//                    mainHandler.postDelayed(this, 2000)
//                }else{
//                    downloadInProgress = false
//                }
//            }
//        })

        setContent {
            InstaSaveTheme {
                MainScreen()
            }
        }
    }
//
//
//    fun downloadMpd(){
//
//        val file = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "manifestt.mpd"
//        )
//
//        Thread {
//            try {
//                val url =
//                    URL("https://scontent-frt3-1.cdninstagram.com/hvideo-ncg-atn/_nc_cat-102/v/rhReDaxdjPHbzfCSbGxNX/_nc_ohc-KUn7DrXy0T8AX8TMki8/live-dash/dash-abr/17936948225154977.mpd?ccb=2-4&ms=m_CN&oh=00_AT8yuAnozHdnEF2VgNSLUg4WnDd_584xZlyXrAkVrSjLwQ&oe=62929FCC")
//                connection = url.openConnection() as HttpURLConnection
//                connection!!.connect()
//
//                input = connection!!.inputStream
//                output = file.outputStream()
//
//                val data = ByteArray(4096)
//                var count: Int
//                while (input!!.read(data).also { count = it } != -1) {
//
//                    output!!.write(data, 0, count)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                try {
//                    output?.close()
//                    input?.close()
//                    Timber.d("downloadMpd Finished")
//
//
//                    val parsedData = parse(file.inputStream())
//
//                    Timber.d("XmlParse %s", parsedData)
//
//                    val baseUrl =
//                        "https://scontent-frt3-1.cdninstagram.com/hvideo-ncg-atn/_nc_cat-102/v/rhReDaxdjPHbzfCSbGxNX/_nc_ohc-KUn7DrXy0T8AX8TMki8/live-dash"
//
//                    //val mediaUrl = parsedData[0].adaptationSet[0].representation[0].segmentTemplate[0].media
//
//                    //val urlList = mutableListOf<String>()
//
//                    if (parsedData[0].adaptationSet[0].representation[0].segmentTemplate[0].segmentTimeline[0].s[0].t!!.toLong() > lastVideoTimeStamp){
//                        refreshDuplicateCount = 0
//                        videoUrlList.addAll(
//                            parsedData[0].adaptationSet[0].representation[0].segmentTemplate[0].segmentTimeline[0].s.map {
//                                var mediaUrl =
//                                    parsedData[0].adaptationSet[0].representation[0].segmentTemplate[0].media
//                                mediaUrl = mediaUrl?.replace("..", baseUrl)
//                                mediaUrl = mediaUrl?.replace("\$Time$", it.t.toString())
//                                lastVideoTimeStamp = it.t!!.toLong()
//                                return@map mediaUrl
//                            }
//                        )
//                    }
//                    else{
//                        refreshDuplicateCount ++
//                    }
//
//                    if (!videoInitDownloaded){
//                        videoInitDownloaded = true
//                        var initVideoUrl =
//                            parsedData[0].adaptationSet[0].representation[0].segmentTemplate[0].initialization
//                        initVideoUrl = initVideoUrl?.replace("..", baseUrl)
//                        videoUrlList.add(
//                            0,
//                            initVideoUrl
//                        )
//
//                        downloadVideoSegments(videoUrlList)
//
//                    }
//
//                    if (parsedData[0].adaptationSet.last().representation[0].segmentTemplate[0].segmentTimeline[0].s[0].t!!.toLong() > lastAudioTimeStamp){
//                        refreshDuplicateCount = 0
//                        audioUrlList.addAll(parsedData[0].adaptationSet.last().representation[0].segmentTemplate[0].segmentTimeline[0].s.map {
//                            var mediaUrl =
//                                parsedData[0].adaptationSet.last().representation[0].segmentTemplate[0].media
//                            mediaUrl = mediaUrl?.replace("..", baseUrl)
//                            mediaUrl = mediaUrl?.replace("\$Time$", it.t.toString())
//                            lastAudioTimeStamp = it.t!!.toLong()
//                            return@map mediaUrl
//                        })
//                    }
//
//                    else{
//                        refreshDuplicateCount ++
//                    }
//
//
//                    if(!audioInitDownloaded){
//                        audioInitDownloaded = true
//                        var initAudioUrl =
//                            parsedData[0].adaptationSet.last().representation[0].segmentTemplate[0].initialization
//                        initAudioUrl = initAudioUrl?.replace("..", baseUrl)
//                        audioUrlList.add(
//                            0,
//                            initAudioUrl
//                        )
//
//                        downloadAudioSegments(audioUrlList)
//
//                    }
//
//
//                } catch (ignored: IOException) {
//                }
//
//                connection?.disconnect();
//            }
//        }.start()
//
//
//    }
//
//    fun downloadVideoSegments(list: MutableList<String?>) {
//        var input: InputStream? = null
//        var output: OutputStream? = null
//        var connection: HttpURLConnection? = null
//
//        val file = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "video.m4v"
//        )
//
//        Thread {
//
//            while (downloadInProgress){
//                if (list.isNotEmpty()) {
//                    val it = list[0]
//
//                    if (it?.isNotEmpty() == true){
//                        try {
//                            val url = URL(it)
//                            connection = url.openConnection() as HttpURLConnection
//                            connection!!.connect()
//
//                            input = connection!!.inputStream
//                            output = FileOutputStream(file, true)
//
//                            val data = ByteArray(4096)
//                            var count: Int
//                            while (input!!.read(data).also { count = it } != -1) {
//                                output!!.write(data, 0, count)
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        } finally {
//                            try {
//                                Timber.d("downloadSegments %s", it)
//
//                                output?.close()
//                                input?.close()
//
//                                list.removeAt(0)
//
//                            } catch (ignored: IOException) {
//                            }
//
//                            connection?.disconnect();
//                        }
//                    }
//
//                }
//
//            }
//
//        }.start()
//    }
//
//    fun downloadAudioSegments(list: MutableList<String?>) {
//        var input: InputStream? = null
//        var output: OutputStream? = null
//        var connection: HttpURLConnection? = null
//
//        val file = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "audio.m4a"
//        )
//
//        Thread {
//
//            while (downloadInProgress){
//                if (list.isNotEmpty()) {
//                    val it = list[0]
//                    if (it?.isNotEmpty() == true){
//                        try {
//                            val url = URL(it)
//                            connection = url.openConnection() as HttpURLConnection
//                            connection!!.connect()
//
//                            input = connection!!.inputStream
//                            output = FileOutputStream(file, true)
//
//                            val data = ByteArray(4096)
//                            var count: Int
//                            while (input!!.read(data).also { count = it } != -1) {
//                                output!!.write(data, 0, count)
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        } finally {
//                            try {
//                                Timber.d("downloadAudioSegments %s", it)
//
//                                output?.close()
//                                input?.close()
//
//                                list.removeAt(0)
//
//                            } catch (ignored: IOException) {
//                            }
//
//                            connection?.disconnect();
//                        }
//                    }
//
//                }
//            }
//
//        }.start()
//    }
//
//
//    private val ns: String? = null
//
//    fun parse(inputStream: InputStream): List<Period> {
//        inputStream.use { inputStream ->
//            val parser: XmlPullParser = Xml.newPullParser()
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
//            parser.setInput(inputStream, null)
//            parser.nextTag()
//            return readMPD(parser)
//        }
//    }
//
//    private fun readMPD(parser: XmlPullParser): List<Period> {
//        val periods = mutableListOf<Period>()
//
//        parser.require(XmlPullParser.START_TAG, ns, "MPD")
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//            // Starts by looking for the entry tag
//            if (parser.name == "Period") {
//                periods.add(readPeriod(parser))
//            } else {
//                skip(parser)
//            }
//        }
//        return periods
//    }
//
//    private fun readPeriod(parser: XmlPullParser): Period {
//        parser.require(XmlPullParser.START_TAG, ns, "Period")
//
//        val adaptationSet: MutableList<AdaptationSet> = mutableListOf()
//
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//
//            if (parser.name == "AdaptationSet") {
//                adaptationSet.add(readAdaptationSet(parser))
//            } else {
//                skip(parser)
//            }
//
//        }
//        return Period(adaptationSet)
//    }
//
//    private fun readAdaptationSet(parser: XmlPullParser): AdaptationSet {
//
//        parser.require(XmlPullParser.START_TAG, ns, "AdaptationSet")
//
//        val representation: MutableList<Representation> = mutableListOf()
//
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//
//            if (parser.name == "Representation") {
//                representation.add(readRepresentation(parser))
//            } else {
//                skip(parser)
//            }
//
//        }
//
//        representation.sortByDescending { it.width }
//
//        return AdaptationSet(representation)
//    }
//
//    private fun readRepresentation(parser: XmlPullParser): Representation {
//
//        parser.require(XmlPullParser.START_TAG, ns, "Representation")
//
//        val id = parser.getAttributeValue(null, "id")
//        val mimeType = parser.getAttributeValue(null, "mimeType")
//        val width = parser.getAttributeValue(null, "width")
//        val height = parser.getAttributeValue(null, "height")
//
//        val segmentTemplate: MutableList<SegmentTemplate> = mutableListOf()
//
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//
//            if (parser.name == "SegmentTemplate") {
//                segmentTemplate.add(readSegmentTemplate(parser))
//            } else {
//                skip(parser)
//            }
//
//        }
//
//        return Representation(id, mimeType, width, height, segmentTemplate)
//    }
//
//    private fun readSegmentTemplate(parser: XmlPullParser): SegmentTemplate {
//        parser.require(XmlPullParser.START_TAG, ns, "SegmentTemplate")
//
//        val segmentTimeline: MutableList<SegmentTimeline> = mutableListOf()
//
//        val initialization = parser.getAttributeValue(null, "initialization")
//        val media = parser.getAttributeValue(null, "media")
//
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//
//            if (parser.name == "SegmentTimeline") {
//                segmentTimeline.add(readSegmentTimeline(parser))
//            } else {
//                skip(parser)
//            }
//
//        }
//
//        return SegmentTemplate(initialization, media, segmentTimeline)
//    }
//
//    private fun readSegmentTimeline(parser: XmlPullParser): SegmentTimeline {
//        parser.require(XmlPullParser.START_TAG, ns, "SegmentTimeline")
//
//        val timelines: MutableList<S> = mutableListOf()
//
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//            Timber.d("readSegmentTimeline %s", parser.name)
//
//            if (parser.name == "S") {
//                timelines.add(readS(parser))
//            } else {
//                skip(parser)
//            }
//
//        }
//        return SegmentTimeline(timelines)
//    }
//
//    private fun readS(parser: XmlPullParser): S {
//
//        parser.require(XmlPullParser.START_TAG, ns, "S")
//
//        val t = parser.getAttributeValue(null, "t")
//        val d = parser.getAttributeValue(null, "d")
//
//        parser.next()
//
//        return S(t, d)
//    }
//
//    private fun readText(parser: XmlPullParser): String {
//        var result = ""
//        if (parser.next() == XmlPullParser.TEXT) {
//            result = parser.text
//            parser.nextTag()
//        }
//        return result
//    }
//
//    private fun skip(parser: XmlPullParser) {
//        if (parser.eventType != XmlPullParser.START_TAG) {
//            throw IllegalStateException()
//        }
//        var depth = 1
//        while (depth != 0) {
//            when (parser.next()) {
//                XmlPullParser.END_TAG -> depth--
//                XmlPullParser.START_TAG -> depth++
//            }
//        }
//    }
//
//    data class Period(
//        val adaptationSet: List<AdaptationSet>
//    )
//
//    data class AdaptationSet(
//        val representation: List<Representation>
//    )
//
//    data class Representation(
//        val id: String?,
//        val mimeType: String?,
//        val width: String?,
//        val height: String?,
//        val segmentTemplate: List<SegmentTemplate>
//    )
//
//    data class SegmentTemplate(
//        val initialization: String?,
//        val media: String?,
//        val segmentTimeline: List<SegmentTimeline>
//    )
//
//    data class SegmentTimeline(
//        val s: List<S>
//    )
//
//    data class S(
//        val t: String?,
//        val d: String?
//    )

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
            composable(BottomNavItem.Home.screenRoute) {
                HomeScreen()
            }
            composable(BottomNavItem.Search.screenRoute) {
                SearchScreen()
            }
            composable(BottomNavItem.Download.screenRoute) {
                DownloadScreen()
            }
            composable(BottomNavItem.Story.screenRoute) {
                StoryScreen()
            }
            composable(BottomNavItem.LiveStream.screenRoute) {
                LiveStreamScreen()
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Search,
            BottomNavItem.Download,
            BottomNavItem.Story,
            BottomNavItem.LiveStream
        )
        androidx.compose.material.BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary
        ) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->

                val selected = currentRoute == item.screenRoute

                BottomNavigationItem(
                    icon = {
                        Icon(
                            if (selected) painterResource(id = item.iconSolid) else painterResource(
                                id = item.iconLight
                            ),
                            contentDescription = item.title,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.primary,
                    alwaysShowLabel = false,
                    selected = selected,
                    onClick = {
                        navController.navigate(item.screenRoute) {

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) }
        ) {
            Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, it.calculateBottomPadding())) {
                NavigationGraph(navController = navController)
            }
        }
    }
}

