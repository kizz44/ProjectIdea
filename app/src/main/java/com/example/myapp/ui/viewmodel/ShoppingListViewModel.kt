package com.example.myapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.ShoppingDatabase
import com.example.myapp.data.model.ShoppingItem
import com.example.myapp.data.model.ShoppingList
import com.example.myapp.data.repository.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ShoppingRepository
    val allLists: LiveData<List<ShoppingList>>

    init {
        val shoppingDao = ShoppingDatabase.getDatabase(application).shoppingDao()
        repository = ShoppingRepository(shoppingDao)
        allLists = repository.allLists
    }

    // Методы для работы со списками
    fun insertList(shoppingList: ShoppingList) = viewModelScope.launch {
        repository.insertList(shoppingList)
    }

    fun updateList(shoppingList: ShoppingList) = viewModelScope.launch {
        repository.updateList(shoppingList)
    }

    fun deleteList(shoppingList: ShoppingList) = viewModelScope.launch {
        repository.deleteList(shoppingList)
    }

    // Методы для работы с товарами
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>> {
        return repository.getItemsForList(listId)
    }

    // Синхронное получение списка товаров
    fun getItemsForListSync(listId: Int): List<ShoppingItem> = runBlocking(Dispatchers.IO) {
        repository.getItemsForListSync(listId)
    }

    fun insertItem(item: ShoppingItem) = viewModelScope.launch {
        repository.insertItem(item)
    }

    fun updateItem(item: ShoppingItem) = viewModelScope.launch {
        repository.updateItem(item)
    }

    fun deleteItem(item: ShoppingItem) = viewModelScope.launch {
        repository.deleteItem(item)
    }

    fun getItemCount(listId: Int): LiveData<Int> {
        return repository.getItemCount(listId)
    }

    fun getCheckedItemCount(listId: Int): LiveData<Int> {
        return repository.getCheckedItemCount(listId)
    }
} 