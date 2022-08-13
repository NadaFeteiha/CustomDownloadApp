package com.nadafeteiha.customdownloadapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.nadafeteiha.customdownloadapp.databinding.ActivityDetailBinding
import com.nadafeteiha.customdownloadapp.databinding.ActivityMainBinding
import com.nadafeteiha.customdownloadapp.util.Constants

class DetailActivity : AppCompatActivity(), OnClickListener {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private lateinit var fileName: String
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getExtraData()
        setData()
    }

    private fun getExtraData() {
        fileName = intent.getStringExtra(Constants.FILE_NAME) ?: ""
        status = intent.getStringExtra(Constants.STATUS) ?: ""
    }

    private fun setData() {
        binding.detailLayout.apply {
            fileNameTextValue.text = fileName
            statusTextValue.text = status
            buttonOk.setOnClickListener(this@DetailActivity)
        }
    }

    override fun onClick(p0: View?) {
        finish()
    }
}
