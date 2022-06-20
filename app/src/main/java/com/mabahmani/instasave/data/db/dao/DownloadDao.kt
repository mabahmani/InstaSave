package com.mabahmani.instasave.data.db.dao

import androidx.room.*
import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(downloadEntity: DownloadEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(downloadEntities: List<DownloadEntity>) : List<Long>

    @Update
    suspend fun update(downloadEntity: DownloadEntity)

    @Delete
    suspend fun delete(downloadEntity: DownloadEntity)

    @Query("SELECT * FROM downloadentity WHERE id == :id")
    fun findById(id: Int): Flow<DownloadEntity>

    @Query("SELECT * FROM downloadentity ORDER BY createdAt DESC")
    fun getAll(): Flow<List<DownloadEntity>>

    @Query("SELECT EXISTS(SELECT * FROM downloadentity WHERE code = :code)")
    suspend fun isExist(code : String) : Boolean

    @Query("UPDATE downloadentity SET downloadStatus=:downloadStatus WHERE fileId = :fileId")
    suspend fun updateDownloadStatus(fileId:String, downloadStatus: String)

    @Query("UPDATE downloadentity SET downloadStatus=:downloadStatus, filePath=:filePath, fileName=:fileName, contentLength=:fileLength WHERE fileId = :fileId")
    suspend fun updateDownloadInfo(fileId:String, downloadStatus: String, filePath: String, fileName: String, fileLength: Long)

    @Query("DELETE FROM downloadentity WHERE fileId = :fileId")
    suspend fun deleteDownload(fileId:String)
}