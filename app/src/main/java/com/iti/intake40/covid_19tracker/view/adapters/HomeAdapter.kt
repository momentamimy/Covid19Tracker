package com.iti.intake40.covid_19tracker.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.intake40.covid_19tracker.data.model.COVID
import kotlinx.android.synthetic.main.covidcell.view.*
import android.text.style.UnderlineSpan
import android.text.SpannableString


class HomeAdapter(private var dataList: List<COVID>, private val context: Context) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                com.iti.intake40.covid_19tracker.R.layout.covidcell,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // country name
        val content = SpannableString(dataList[position].countryName)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        holder.itemView.countryName.text = content

        holder.itemView.countryName.setOnClickListener(View.OnClickListener {
          //  holder.itemView.countryName.setTextColor(Color.RED)
        })
        //confirmed
        holder.itemView.confirmed.text = dataList[position].cases
        //recovered
        holder.itemView.recovered.text = dataList[position].recover
        //death
        holder.itemView.death.text = dataList[position].deaths
    }


    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView)
}