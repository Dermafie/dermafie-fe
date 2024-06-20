package com.example.dermafie.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dermafie.R
import com.example.dermafie.data.response.DataItem
import com.example.dermafie.ui.result.ResultActivity
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Locale


class HistoryAdapter(
    private val historyList: List<DataItem>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.scanDateTextView.text = item.scanDate
        holder.scanResultTextView.text = item.scanResult

        Glide.with(holder.itemView.context)
            .load(item.imageURL)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ResultActivity::class.java).apply {
                putExtra("resultMessage", item.scanResult)
                putExtra("diseaseDescription", item.disease.description)
                putExtra("diseaseEffects", item.disease.effects)
                putExtra("diseaseSolution", item.disease.solution)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scanDateTextView: TextView = itemView.findViewById(R.id.tv_item_name)
        val scanResultTextView: TextView = itemView.findViewById(R.id.tv_item_description)
        val imageView: ImageView = itemView.findViewById(R.id.img_item_photo)
    }
}
