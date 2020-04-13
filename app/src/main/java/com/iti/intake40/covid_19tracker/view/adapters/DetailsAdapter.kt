package com.iti.intake40.covid_19tracker.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iti.intake40.covid_19tracker.R
import kotlinx.android.synthetic.main.detailcell.view.*


class DetailsAdapter(private var data:Map<String,Any>, private val context: Context) :
    RecyclerView.Adapter<DetailsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.detailcell,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
             if (position == 2 || position == 5)
             {
                 holder.itemView.detailValue.background = ContextCompat.getDrawable(context,R.drawable.border3)
             }
             holder.itemView.detailType.text = data.keys.elementAt(position)
             holder.itemView.detailValue.text = data.values.elementAt(position).toString()

    }


    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView)
}