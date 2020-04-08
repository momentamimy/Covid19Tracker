package com.iti.intake40.covid_19tracker.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.intake40.covid_19tracker.R
import com.iti.intake40.covid_19tracker.data.model.COVID
import kotlinx.android.synthetic.main.covidcell.view.*

class HomeAdapter(private var dataList: List<COVID>, private val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.covidcell, parent, false))
    }
    override fun getItemCount(): Int =  dataList.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
             holder.itemView.countryName.text = dataList[position].countryName
             holder.itemView.confirmed.text = dataList[position].cases
             holder.itemView.recovered.text = dataList[position].recover
             holder.itemView.death.text = dataList[position].deaths
    }


    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView)
}