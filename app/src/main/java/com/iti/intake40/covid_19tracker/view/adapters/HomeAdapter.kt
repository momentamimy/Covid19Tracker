package com.iti.intake40.covid_19tracker.view.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.intake40.covid_19tracker.data.model.COVID
import kotlinx.android.synthetic.main.covidcell.view.*
import android.text.style.UnderlineSpan
import android.text.SpannableString
import android.widget.Filter
import android.widget.Filterable
import com.iti.intake40.covid_19tracker.view.activities.DetailsActivity
import java.util.*


class HomeAdapter(private var dataList: List<COVID>, private val context: Context) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>(), Filterable {
    private var dataFilterList = listOf<COVID>()
    init {
        dataFilterList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                com.iti.intake40.covid_19tracker.R.layout.covidcell,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataFilterList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // country name
        val content = SpannableString(dataFilterList[position].countryName)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        holder.itemView.countryName.text = content

        holder.itemView.countryName.setOnClickListener(View.OnClickListener {
            val detailIntent = Intent(context,DetailsActivity::class.java)
            detailIntent.putExtra("covid",dataFilterList[position])
            context.startActivity(detailIntent)
        })
        //confirmed
        holder.itemView.confirmed.text = dataFilterList[position].cases
        //recovered
        holder.itemView.recovered.text = dataFilterList[position].recover
        //death
        holder.itemView.death.text = dataFilterList[position].deaths
    }

    override fun getFilter(): Filter {

         return object : Filter(){
             override fun performFiltering(constraint: CharSequence?): FilterResults {
                 val charSearch = constraint.toString()
                 if (charSearch.isEmpty()) {

                     dataFilterList = dataList
                 }
                 else {
                     val resultList =mutableListOf<COVID>()

                     for (row in dataList) {
                         if (row.countryName.toLowerCase(Locale.ROOT).startsWith(charSearch.toLowerCase(Locale.ROOT),true)) {

                             resultList.add(row)
                         }
                     }
                     dataFilterList = resultList

                 }
                 val filterResults = FilterResults()
                 filterResults.values = dataFilterList
                 return filterResults
             }
             @Suppress("UNCHECKED_CAST")
             override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                 dataFilterList = results?.values as List<COVID>

                 notifyDataSetChanged()
             }
         }
    }


    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView)
}