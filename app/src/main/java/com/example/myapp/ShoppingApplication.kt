package com.example.myapp

import android.app.Application
import com.example.myapp.data.ShoppingDatabase

class ShoppingApplication : Application() {
    val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(this) }
} 