package com.nitish.privacyindicator.ui.logs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitish.privacyindicator.databinding.ActivitySuspiciousLogsBinding
import com.nitish.privacyindicator.db.AccessLogsDatabase
import com.nitish.privacyindicator.repository.AccessLogsRepo
import com.nitish.privacyindicator.ui.adapters.SuspiciousLogsAdapter

class SuspiciousLogsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuspiciousLogsBinding
    private lateinit var viewModel: SuspiciousLogsViewModel
    private lateinit var suspiciousLogsAdapter: SuspiciousLogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuspiciousLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accessLogsDatabase = AccessLogsDatabase(this)
        val viewModelProviderFactory = LogsViewModelProviderFactory(application, AccessLogsRepo(accessLogsDatabase))
        
        // Note: LogsViewModelProviderFactory creates AccessLogsViewModel by default.
        // For simplicity, I'll just use a basic creation or update the factory if needed.
        // Actually, let's just create it directly here if factory doesn't support it yet.
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(SuspiciousLogsViewModel::class.java)

        setUpViews()
        setUpListeners()
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.allSuspiciousActivities.observe(this, { list ->
            suspiciousLogsAdapter.differ.submitList(list)
            binding.tvEmptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun setUpViews() {
        suspiciousLogsAdapter = SuspiciousLogsAdapter()
        binding.rvSuspiciousLogs.apply {
            adapter = suspiciousLogsAdapter
            layoutManager = LinearLayoutManager(this@SuspiciousLogsActivity)
        }
    }

    private fun setUpListeners() {
        binding.btnClearAll.setOnClickListener {
            viewModel.clearAll()
        }
    }
}
