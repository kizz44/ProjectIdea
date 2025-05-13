package com.example.myapp.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.data.model.ShoppingItem

class ShoppingItemAdapter(
    private val onItemChecked: (ShoppingItem) -> Unit,
    private val onItemDeleted: (ShoppingItem) -> Unit
) : ListAdapter<ShoppingItem, ShoppingItemAdapter.ViewHolder>(ShoppingItemDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val noteTextView: TextView = view.findViewById(R.id.noteTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.checkBox.isChecked = item.isChecked
        holder.nameTextView.text = item.name
        holder.quantityTextView.text = "x${item.quantity}"
        holder.noteTextView.text = item.note
        holder.noteTextView.visibility = if (item.note.isBlank()) View.GONE else View.VISIBLE

        // Strike through text if item is checked
        if (item.isChecked) {
            holder.nameTextView.paintFlags = holder.nameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.quantityTextView.paintFlags = holder.quantityTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.nameTextView.paintFlags = holder.nameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.quantityTextView.paintFlags = holder.quantityTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.checkBox.setOnClickListener {
            onItemChecked(item.copy(isChecked = holder.checkBox.isChecked))
        }

        holder.deleteButton.setOnClickListener {
            onItemDeleted(item)
        }
    }

    private class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }
} 