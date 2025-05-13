package com.example.myapp.data.repository

import androidx.lifecycle.LiveData
import com.example.myapp.data.dao.ShoppingDao
import com.example.myapp.data.model.ShoppingItem
import com.example.myapp.data.model.ShoppingList

class ShoppingRepository(private val shoppingDao: ShoppingDao) {
    // Shopping Lists
    val allLists: LiveData<List<ShoppingList>> = shoppingDao.getAllLists()

    suspend fun insertList(shoppingList: ShoppingList): Long {
        return shoppingDao.insertList(shoppingList)
    }
    
    suspend fun updateList(shoppingList: ShoppingList) {
        shoppingDao.updateList(shoppingList)
    }
    
    suspend fun deleteList(shoppingList: ShoppingList) {
        shoppingDao.deleteList(shoppingList)
    }

    // Shopping Items
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>> {
        return shoppingDao.getItemsForList(listId)
    }
    
    suspend fun getItemsForListSync(listId: Int): List<ShoppingItem> {
        return shoppingDao.getItemsForListSync(listId)
    }
    
    suspend fun insertItem(shoppingItem: ShoppingItem): Long {
        return shoppingDao.insertItem(shoppingItem)
    }
    
    suspend fun updateItem(shoppingItem: ShoppingItem) {
        shoppingDao.updateItem(shoppingItem)
    }
    
    suspend fun deleteItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteItem(shoppingItem)
    }
    
    suspend fun updateItemCheckedStatus(itemId: Int, isChecked: Boolean) {
        shoppingDao.updateItemCheckedStatus(itemId, isChecked)
    }

    fun getItemCount(listId: Int): LiveData<Int> {
        return shoppingDao.getItemCount(listId)
    }

    fun getCheckedItemCount(listId: Int): LiveData<Int> {
        return shoppingDao.getCheckedItemCount(listId)
    }
} 