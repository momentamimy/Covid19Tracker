package com.iti.intake40.covid_19tracker.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.intake40.covid_19tracker.R
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.view.adapters.DetailsAdapter
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private var subscribeFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setupViews()
    }


    private fun setupViews()
    {
        detailsRecycle.layoutManager =  LinearLayoutManager(this)
        val covid = intent.getSerializableExtra("covid") as COVID
        detailsRecycle.adapter = DetailsAdapter(covid.toMap(),this)

        // here check if this country subscribed or not
        // now i put it unsubscribed until room created
        subscribeBtn.setImageResource(R.drawable.unsubscribe)
        subscribeFlag = false
    }

    fun subscribe(view: View) {
       if(subscribeFlag)
       {
           // unsubscribe
           subscribeBtn.setImageResource(R.drawable.unsubscribe)
           subscribeFlag = false
       }
        else
       {
           // subscribe
           subscribeBtn.setImageResource(R.drawable.subscribe)
           subscribeFlag = true
       }
    }
}
