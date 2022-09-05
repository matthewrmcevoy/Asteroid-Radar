package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.TextItemViewHolder

class AsteroidLinearAdapter : RecyclerView.Adapter<AsteroidLinearAdapter.ViewHolder>() {
    var data = listOf<Asteroid>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        //val res = holder.itemView.context.resources
        holder.asteroidCodeName.text = item.codename
        holder.asteroidCADate.text = item.closeApproachDate
        holder.asteroidHazard.setImageResource(
            when(item.isPotentiallyHazardous){
                true -> R.drawable.ic_status_potentially_hazardous
                else -> R.drawable.ic_status_normal
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.linear_list_item, parent, false)

        return ViewHolder(view)
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val asteroidCodeName: TextView = itemView.findViewById(R.id.code_name_txt)
        val asteroidCADate: TextView = itemView.findViewById(R.id.nearest_approach_txt)
        val asteroidHazard: ImageView = itemView.findViewById(R.id.potential_hazard_image)
    }
}

