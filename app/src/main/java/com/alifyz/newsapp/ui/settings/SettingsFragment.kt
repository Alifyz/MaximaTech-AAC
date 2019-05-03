package com.alifyz.newsapp.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                    Toast.makeText(context, "Work Agendado", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.BLOCKED -> {
                    Toast.makeText(context, "Work Bloqueado", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.CANCELLED -> {
                    Toast.makeText(context, "Work Cancelado", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.FAILED -> {
                    Toast.makeText(context, "Work Falhou", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.RUNNING -> {
                    Toast.makeText(context, "Work Rodando", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.SUCCEEDED -> {
                    Toast.makeText(context, "Work Concluído", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}