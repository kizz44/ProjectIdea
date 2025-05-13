package com.example.myapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {
    // Shopping Lists operations
    @Insert
    suspend fun insertList(shoppingList: ShoppingList)

    @Update
    suspend fun updateList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteList(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_lists ORDER BY name ASC")
    fun getAllLists(): LiveData<List<ShoppingList>>

    // Shopping Items operations
    @Insert
    suspend fun insertItem(shoppingItem: ShoppingItem)

    @Update
    suspend fun updateItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isChecked ASC, name ASC")
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>>

    @Query("DELETE FROM shopping_items WHERE listId = :listId")
    suspend fun deleteItemsForList(listId: Int)
} 