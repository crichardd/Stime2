package com.example.stime

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stime2.R

class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rank: TextView = itemView.findViewById(R.id.rank)
    val appid: TextView = itemView.findViewById(R.id.appid)
    val last_week_rank: TextView = itemView.findViewById(R.id.last_week_rank)
    val peak_in_game: TextView = itemView.findViewById(R.id.peak_in_game)
    val name: TextView = itemView.findViewById(R.id.name)
}