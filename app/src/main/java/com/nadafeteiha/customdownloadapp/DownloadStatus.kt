package com.nadafeteiha.customdownloadapp

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity

enum class Status {
    FAILURE,
    SUCCESS
}

class DownloadStatus(val context: Context) {

    fun isDownloadCompleted(intent: Intent?, downloadID: Long): Boolean {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        return id == downloadID && intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE
    }

    fun getDownloadStatus(intent: Intent?): Status {
        var status = Status.FAILURE
        val query = DownloadManager.Query()
        intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)?.let { query.setFilterById(it) }
        val manager =
            context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager

        val cursor: Cursor = manager.query(query)
        if (cursor.moveToFirst()) {
            if (cursor.count > 0) {
                val statusInt = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (statusInt == DownloadManager.STATUS_SUCCESSFUL) {
                    status = Status.SUCCESS
                }
            }
        }
        return status
    }
}