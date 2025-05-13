package com.example.myapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingItemAdapter(
    private val onItemChecked: (ShoppingItem) -> Unit,
    private val onItemDeleted: (ShoppingItem) -> Unit
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    private var items = listOf<ShoppingItem>()

    inner class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(item: ShoppingItem) {
            checkBox.isChecked = item.isChecked
            nameTextView.text = item.name
            quantityTextView.text = "x${item.quantity}"
            noteTextView.text = item.note
            noteTextView.visibility = if (item.note.isBlank()) View.GONE else View.VISIBLE

            // Strike through text if item is checked
            if (item.isChecked) {
                nameTextView.paintFlags = nameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                quantityTextView.paintFlags = quantityTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                nameTextView.paintFlags = nameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                quantityTextView.paintFlags = quantityTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            checkBox.setOnClickListener {
                onItemChecked(item.copy(isChecked = checkBox.isChecked))
            }

            deleteButton.setOnClickListener {
                onItemDeleted(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_item, parent, false)
        return ShoppingItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<ShoppingItem>) {
        items = newItems
        notifyDataSetChanged()
    }
} 