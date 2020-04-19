package com.iti.intake40.covid_19tracker.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.work.*
import java.util.concurrent.TimeUnit


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Setting) {
            showAlertdialog("update period")
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        loadDataLocal()

    }

    private fun checkIntent() {
        if (intent.getSerializableExtra("covid") != null) {
            val covid = intent.getSerializableExtra("covid") as COVID
            val detailIntent = Intent(this, DetailsActivity::class.java)
            detailIntent.putExtra("covid", covid)
            startActivity(detailIntent)
        }
    }

    private fun setupViews() {
        progressDialog = ProgressDialog(this)
        covidRecycle.layoutManager = LinearLayoutManager(this)
        covidRecycle.adapter = adapter
        noResultLayout.visibility = View.GONE
        swipeContainer.setOnRefreshListener { reloadData() }
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
                    Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_LONG)
                        .show()
                    swipeContainer.isRefreshing = false
                } else {
                    swipeContainer.isRefreshing = false
                }
            })
    }


    private fun loadDataLocal() {
        viewModel.getLocalData(application)?.observe(this,
            Observer<List<COVID>> { data ->
                if (data.isEmpty()) {
                    showReloadMessage()
                    val pair: Pair<Long?, String?> = viewModel.getPeriod(this)
                    var positionNumber: Long? = pair.first
                    var positionDate: TimeUnit = TimeUnit.HOURS
                    if (pair.second.equals("Day")) {
                        positionDate = TimeUnit.DAYS
                    } else if (pair.second.equals("Hour")) {
                        positionDate = TimeUnit.HOURS
                    }
                    val request = viewModel.createPeriodicWork(application, positionNumber!!,positionDate )
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

    fun showAlertdialog(title: String) {
        val view = layoutInflater.inflate(R.layout.period_dialog, null)
        val numberPicker: NumberPicker = view.findViewById(R.id.Number)
        val datePicker: NumberPicker = view.findViewById(R.id.Date)

        numberPicker.minValue = 1
        numberPicker.maxValue = 30
        val strings = arrayOf("Hour", "Day")
        datePicker.displayedValues = strings
        datePicker.minValue = 0
        datePicker.maxValue = strings.size - 1

        val pair: Pair<Long?, String?> = viewModel.getPeriod(this)
        var positionNumber: Long? = pair.first
        var positionDate: TimeUnit = TimeUnit.HOURS
        if (pair.second.equals("Day")) {
            positionDate = TimeUnit.DAYS
            datePicker.value=1
        } else if (pair.second.equals("Hour")) {
            positionDate = TimeUnit.HOURS
            datePicker.value=0
        }
        numberPicker.value=positionNumber!!.toInt()


        datePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            if (newVal == 0)
                positionDate = TimeUnit.HOURS
            else if (newVal == 1)
                positionDate = TimeUnit.DAYS
        }
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            positionNumber = newVal.toLong()
        }

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setTitle(title)
        builder.setPositiveButton("Ok") { dialog, which ->

            viewModel.createPeriodicWork(application, positionNumber!!, positionDate)

            if (positionDate.equals(TimeUnit.DAYS)) {
                viewModel.savePeriod(this, positionNumber!!, "Day")
            } else if (positionDate.equals(TimeUnit.HOURS)) {
                viewModel.savePeriod(this, positionNumber!!, "Hour")
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
