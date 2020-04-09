package com.iti.intake40.covid_19tracker.view.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.iti.intake40.covid_19tracker.R
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.view.adapters.HomeAdapter
import com.iti.intake40.covid_19tracker.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var dataList = listOf<COVID>()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupViews()
        showProgressDialog()
    }

    override fun onStart() {
        super.onStart()
        loadDataLocal()
    }

    private fun setupViews() {
        covidRecycle.layoutManager = LinearLayoutManager(this)
        covidRecycle.adapter = HomeAdapter(dataList, this)
        reloadButton.setOnClickListener{
            showProgressDialog()
            loadData()
        }
    }

    private fun showReloadMessage()
    {
        reloadButton.visibility = View.VISIBLE
        textNoInternet.visibility = View.VISIBLE
        covidRecycle.visibility = View.GONE
    }
    private fun hideReloadMessage()
    {
        reloadButton.visibility = View.GONE
        textNoInternet.visibility = View.GONE
        covidRecycle.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        // progress
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun loadData() {
        viewModel.getData(application).observe(this,
            Observer<List<COVID>> { data ->
                if (data == null) {
                    progressDialog.dismiss()
                    showReloadMessage()
                }
            })
    }

    private fun loadDataLocal() {
        viewModel.getLocalData(application)?.observe(this,
            Observer<List<COVID>> { data ->
                if (data == null) {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Can't load States", Toast.LENGTH_LONG).show()
                } else if (data.isEmpty()) {
                    loadData()
                } else if (data.isNotEmpty()) {
                    dataList = data
                    progressDialog.dismiss()
                    covidRecycle.adapter = HomeAdapter(dataList, this)
                    hideReloadMessage()
                }
            })
    }

}
