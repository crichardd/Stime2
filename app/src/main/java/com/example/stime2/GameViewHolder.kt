package com.example.stime

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stime2.R

class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rank: TextView = itemView.findViewById(R.id.rank)
    val name: TextView = itemView.findViewById(R.id.name)
    val publishers: TextView = itemView.findViewById(R.id.publishers)
    val final_formatted: TextView = itemView.findViewById(R.id.final_formatted)
    val header_image: ImageView = itemView.findViewById(R.id.header_image)
}