package com.dailyweath.feat_weather.presentation.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dailyweath.feat_weather.R
import com.dailyweath.feat_weather.presentation.models.DayUI
import com.dailyweath.feat_weather.presentation.utils.getWeatherIconRes

class DayAdapter(
    private val items: List<DayUI>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dateText)
        val tempConditionsText: TextView = view.findViewById(R.id.tempConditionsText)
        val iconImage: ImageView = view.findViewById(R.id.iconImage)

        fun bind(day: DayUI) {
            dateText.text = day.datetime
            tempConditionsText.text = "${day.temp} - ${day.conditions}"
            iconImage.setImageResource(getWeatherIconRes(day.icon))

            itemView.setOnClickListener { onItemClick(day.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_card, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}