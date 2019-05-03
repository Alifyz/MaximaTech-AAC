package com.alifyz.newsapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.alifyz.newsapp.R
import com.alifyz.newsapp.background.ImageProcessingWorker
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_work.setOnClickListener {
            WorkManager.getInstance().enqueue(OneTimeWorkRequest.from(ImageProcessingWorker::class.java))
        }
    }
}