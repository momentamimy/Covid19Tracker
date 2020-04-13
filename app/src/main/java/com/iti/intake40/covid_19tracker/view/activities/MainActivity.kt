package com.iti.intake40.covid_19tracker.view.activities

import android.app.ProgressDialog
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

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var dataList = listOf<COVID>()
    private var adapter = HomeAdapter(dataList,this)
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
        covidRecycle.adapter = adapter
        noResultLayout.visibility = View.GONE
        reloadButton.setOnClickListener{
            showProgressDialog()
            loadData()
        }
        search()
    }

    private fun showReloadMessage()
    {
        noResultLayout.visibility = View.VISIBLE
        covidRecycle.visibility = View.GONE
        table_header.visibility = View.GONE
    }
    private fun hideReloadMessage()
    {
        noResultLayout.visibility = View.GONE
        covidRecycle.visibility = View.VISIBLE
        table_header.visibility = View.VISIBLE
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
                    refreshRecycle()
                    hideReloadMessage()
                }
            })
    }


    private fun search()
    {
        country_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.trim()?.isEmpty()!!)
                {
                    refreshRecycle()
                }
                else
                {
                    adapter.filter.filter(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText?.trim()?.isEmpty()!!)
                {
                    refreshRecycle()
                }
                else
                {
                    adapter.filter.filter(newText)
                }

                return false
            }

        })
    }

     private fun refreshRecycle()
     {
         adapter =  HomeAdapter(dataList,this)
         covidRecycle.adapter = adapter
     }



}
