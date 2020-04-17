package com.iti.intake40.covid_19tracker.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.iti.intake40.covid_19tracker.R
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.view.adapters.HomeAdapter
import com.iti.intake40.covid_19tracker.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.widget.Switch
import androidx.core.view.isEmpty
import androidx.work.*
import com.iti.intake40.covid_19tracker.service.CreateWorkService
import com.iti.intake40.covid_19tracker.service.LoadFirstWorkerManger


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var dataList = listOf<COVID>()
    private var adapter = HomeAdapter(dataList, this)
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        checkIntent()
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        loadDataLocal()

    }

    private fun checkIntent()
    {
        if (intent.getSerializableExtra("covid")!=null)
        {
            val covid = intent.getSerializableExtra("covid") as COVID
            val detailIntent = Intent(this,DetailsActivity::class.java)
            detailIntent.putExtra("covid",covid)
            startActivity(detailIntent)
        }
    }

    private fun setupViews() {
        progressDialog = ProgressDialog(this)
        covidRecycle.layoutManager = LinearLayoutManager(this)
        covidRecycle.adapter = adapter
        noResultLayout.visibility = View.GONE
        swipeContainer.setOnRefreshListener { viewModel.createPeriodicWork(application)/*reloadData()*/ }
        search()
    }

    private fun showReloadMessage() {
        noResultLayout.visibility = View.VISIBLE
        covidRecycle.visibility = View.GONE
        table_header.visibility = View.GONE
    }

    private fun hideReloadMessage() {
        noResultLayout.visibility = View.GONE
        covidRecycle.visibility = View.VISIBLE
        table_header.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        // progress
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideProgressDialog() {
            progressDialog.dismiss()
    }

    private fun reloadData() {
        viewModel.getData(application).observe(this,
            Observer<List<COVID>> { data ->
                if (data == null) {
                    Toast.makeText(this,"Please check the internet connection",Toast.LENGTH_LONG).show()
                    swipeContainer.isRefreshing=false
                }
                else
                {
                    swipeContainer.isRefreshing=false
                }
            })
    }


    private fun loadDataLocal() {
        viewModel.getLocalData(application)?.observe(this,
            Observer<List<COVID>> { data ->
                if (data.isEmpty()) {
                    showReloadMessage()
                    val request = viewModel.createPeriodicWork(application)
                    WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                        .observe(this, Observer<WorkInfo> { workInfo ->
                            if (workInfo != null && workInfo.state == WorkInfo.State.RUNNING) {
                                println("TAAAAAG RUNNING")
                                showProgressDialog()
                            }
                        })
                } else if (data.isNotEmpty()) {
                    dataList = data
                    hideReloadMessage()
                    hideProgressDialog()
                    refreshRecycle()
                }
            })
    }


    private fun search() {
        country_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.trim()?.isEmpty()!!) {
                    refreshRecycle()
                } else {
                    adapter.filter.filter(query)
                }

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText?.trim()?.isEmpty()!!) {
                    refreshRecycle()
                } else {
                    adapter.filter.filter(newText)
                }

                return false
            }

        })
    }

    private fun refreshRecycle() {
        adapter = HomeAdapter(dataList, this)
        covidRecycle.adapter = adapter
    }


}
