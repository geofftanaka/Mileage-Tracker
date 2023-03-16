package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.ItemRoomDatabase

class DistanceApplication : Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this)}
}