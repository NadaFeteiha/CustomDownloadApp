package com.nadafeteiha.customdownloadapp

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nadafeteiha.customdownloadapp.databinding.ActivityMainBinding
import com.nadafeteiha.customdownloadapp.util.Constants.GLIDE_URL
import com.nadafeteiha.customdownloadapp.util.Constants.RETROFIT_URL
import com.nadafeteiha.customdownloadapp.util.Constants.UDACITY_URL
import com.nadafeteiha.customdownloadapp.util.createNotificationChannel
import com.nadafeteiha.customdownloadapp.util.sendNotification

class MainActivity : AppCompatActivity(), OnClickListener {

    private var downloadID: Long = 0
    private lateinit var fileName: String
    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }
    private val downloadStatus: DownloadStatus by lazy { DownloadStatus(this) }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        binding.layout.customButton.setOnClickListener(this)
        this.createNotificationChannel()

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var status = Status.FAILURE
            if (downloadStatus.isDownloadCompleted(intent, downloadID)) {
                status = downloadStatus.getDownloadStatus(intent)
            }
            binding.layout.customButton.setButState(ButtonState.Completed)
            Snackbar.make(findViewById(android.R.id.content), status.name, Snackbar.LENGTH_LONG).show()
            notificationManager.sendNotification(fileName, this@MainActivity, status)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
    }

    private fun getSelectedURL(): String? {
        return when (binding.layout.downloadGroup.checkedRadioButtonId) {
            R.id.glide_radio_button -> {
                fileName = resources.getString(R.string.option_glide)
                GLIDE_URL
            }
            R.id.udacity_radio_button -> {
                fileName = resources.getString(R.string.option_udacity)
                UDACITY_URL
            }
            R.id.retrofit_radio_button -> {
                fileName = resources.getString(R.string.option_retrofit)
                RETROFIT_URL
            }
            else -> null
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            val url = getSelectedURL()
            url?.let {
                download(url)
                binding.layout.customButton.setButState(ButtonState.Loading)
            } ?: Snackbar.make(
                findViewById(android.R.id.content),
                R.string.option_not_selected,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}