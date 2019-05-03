package com.alifyz.newsapp.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.alifyz.newsapp.R
import com.alifyz.newsapp.background.ImageProcessingWorker
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_work.setOnClickListener {
            viewModel.startWorker()
        }

        viewModel.workerStatus().observe(this, Observer {
            when (it.state) {
                WorkInfo.State.ENQUEUED -> {
                    worker_status.text = "Work Agendado"
                }

                WorkInfo.State.BLOCKED -> {
                    worker_status.text = "Work Bloqueado"
                }

                WorkInfo.State.CANCELLED -> {
                    worker_status.text = "Work Cancelado"
                }

                WorkInfo.State.FAILED -> {
                    worker_status.text = "Work Falhou"
                }

                WorkInfo.State.RUNNING -> {
                    worker_status.text = "Work Rodando"
                }

                WorkInfo.State.SUCCEEDED -> {
                    worker_status.text = "Work Finalizado"
                }
            }
        })
    }
}