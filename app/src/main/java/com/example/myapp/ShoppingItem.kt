package com.example.myapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int,
    var name: String,
    var quantity: Int = 1,
    var isChecked: Boolean = false,
    var note: String = ""
) 