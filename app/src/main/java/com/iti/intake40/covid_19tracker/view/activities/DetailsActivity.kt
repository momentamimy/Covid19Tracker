package com.iti.intake40.covid_19tracker.view.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.intake40.covid_19tracker.R
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.view.adapters.DetailsAdapter
import com.iti.intake40.covid_19tracker.viewModel.DetailsViewModel
import com.iti.intake40.covid_19tracker.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private var subscribeFlag = false
    private lateinit var viewModel: DetailsViewModel
    private lateinit var covid:COVID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        setupViews()
    }


    private fun setupViews()
    {
        detailsRecycle.layoutManager =  LinearLayoutManager(this)
        covid = intent.getSerializableExtra("covid") as COVID
        detailsRecycle.adapter = DetailsAdapter(covid.toMap(),this)

        subscribeFlag = viewModel.isSubscribed(this,covid.countryName)
        if (subscribeFlag)
            subscribeBtn.setImageResource(R.drawable.subscribe)
        else
            subscribeBtn.setImageResource(R.drawable.unsubscribe)

    }

    fun subscribe(view: View) {
       if(subscribeFlag)
       {
           // unsubscribe
           showAlertdialog("Unsubscribe","Do you want to unsubscribe this country ?",false)
       }
        else
       {
           // subscribe
           showAlertdialog("Subscribe","Do you want to subscribe this country ?",true)
       }
    }


    fun showAlertdialog(title:String,description:String,subscribe: Boolean)
    {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setMessage(description)

        builder.setPositiveButton("YES"){dialog, which ->
            if (subscribe)
            {
                viewModel.subscribe(this,covid.countryName)
                subscribeBtn.setImageResource(R.drawable.subscribe)
                subscribeFlag = true
            }
            else
            {
                viewModel.subscribe(this,"")
                subscribeBtn.setImageResource(R.drawable.unsubscribe)
                subscribeFlag = false
            }
        }
        builder.setNegativeButton("No"){dialog,which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
