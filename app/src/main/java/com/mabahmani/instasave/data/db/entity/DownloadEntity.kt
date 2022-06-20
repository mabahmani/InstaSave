package com.mabahmani.instasave.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["fileId", "code"], unique = true)]
)
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var code: String,
    var url: String,
    var filePath: String,
    var fileName: String,
    var fileId: String,
    var userName: String,
    var contentLength: Long,
    var createdAt: Long,
    var previewImageUrl: String,
    var downloadStatus: String,
    var mediaType: String,
    var fullName: String,
    var profilePictureUrl: String,
)
