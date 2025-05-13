package com.example.myapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapp.data.model.ShoppingItem
import com.example.myapp.data.model.ShoppingList

@Dao
interface ShoppingDao {
    // Shopping Lists
    @Query("SELECT * FROM shopping_lists ORDER BY name ASC")
    fun getAllLists(): LiveData<List<ShoppingList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(shoppingList: ShoppingList): Long

    @Update
    suspend fun updateList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteList(shoppingList: ShoppingList)

    // Shopping Items
    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isChecked ASC, name ASC")
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isChecked ASC, name ASC")
    suspend fun getItemsForListSync(listId: Int): List<ShoppingItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItem): Long

    @Update
    suspend fun updateItem(item: ShoppingItem)

    @Delete
    suspend fun deleteItem(item: ShoppingItem)

    @Query("UPDATE shopping_items SET isChecked = :isChecked WHERE id = :itemId")
    suspend fun updateItemCheckedStatus(itemId: Int, isChecked: Boolean)

    @Query("SELECT COUNT(*) FROM shopping_items WHERE listId = :listId")
    fun getItemCount(listId: Int): LiveData<Int>

    @Query("SELECT COUNT(*) FROM shopping_items WHERE listId = :listId AND isChecked = 1")
    fun getCheckedItemCount(listId: Int): LiveData<Int>
} 