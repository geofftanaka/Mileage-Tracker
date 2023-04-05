package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Item

class DistanceListAdapter(private val onClick: (Item) -> Unit) :
    ListAdapter<Item, DistanceListAdapter.DistanceViewHolder>(DistanceDiffCallback) {

    class DistanceViewHolder(itemView: View, val onClick: (Item) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        private val distanceTextView: TextView = itemView.findViewById(R.id.distance_text)
        private var currentItem: Item? = null

        init {
            itemView.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: Item) {
            currentItem = item

            dateTextView.text = item.drivenDate.toString()
            distanceTextView.text = item.distance.toString()
        }

        fun getId() : Int {
            return currentItem?.id ?: -1
        }

        fun getItem() : Item? {
            return currentItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.distance_item, parent, false)
        return DistanceViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: DistanceViewHolder, position: Int) {
        val distanceItem = getItem(position)
        holder.bind(distanceItem)
    }
}

object DistanceDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

}