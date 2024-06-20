package com.example.dermafie.ui

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dermafie.R
import com.example.dermafie.data.response.DataItem
import com.example.dermafie.ui.result.ResultActivity
import com.example.storyapp.data.retrofit.ApiConfig
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import java.util.Locale


class HistoryAdapter : ListAdapter<DataItem, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val scanDateTextView: TextView = itemView.findViewById(R.id.tv_item_name)
        private val scanResultTextView: TextView = itemView.findViewById(R.id.tv_item_description)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dataItem: DataItem) {
            // Format the scanDate to only show the date part
            val imageView: ImageView = itemView.findViewById(R.id.img_item_photo)
            Glide.with(itemView)
                .load(dataItem.imageURL) // Replace with the actual URL or resource of the image
                .placeholder(R.drawable.ic_place_holder) // Placeholder image while loading
                .into(imageView)
            val originalDate = ZonedDateTime.parse(dataItem.scanDate, DateTimeFormatter.ISO_DATE_TIME)
            val formattedDate = originalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            scanDateTextView.text = formattedDate
            scanResultTextView.text = dataItem.scanResult
        }
    }
}
