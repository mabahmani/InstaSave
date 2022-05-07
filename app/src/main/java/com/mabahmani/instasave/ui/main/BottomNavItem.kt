package com.mabahmani.instasave.ui.main

import com.mabahmani.instasave.R

sealed class BottomNavItem(
    var title:String, var iconSolid:Int, var iconLight: Int, var screenRoute:String
){
    object Home : BottomNavItem("Home", R.drawable.ics_house, R.drawable.ic_house, "home")
    object Search : BottomNavItem("Search", R.drawable.ics_magnifying_glass, R.drawable.ic_magnifying_glass, "search")
    object Download : BottomNavItem("Download", R.drawable.ics_download, R.drawable.ic_download, "download")
    object Story : BottomNavItem("Story", R.drawable.ics_rectangle_vertical_history, R.drawable.ic_rectangle_vertical_history, "story")
    object LiveStream : BottomNavItem("LiveStream", R.drawable.ics_tower_broadcast, R.drawable.ic_tower_broadcast, "live")
}
