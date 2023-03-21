package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Item
import com.example.myapplication.data.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*

class DistanceViewModel(private val itemDao: ItemDao) : ViewModel() {
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    fun getDailyTotal() : LiveData<Int> {
        return itemDao.getTotalByDate(LocalDate.now()).asLiveData()
    }

    fun getAnnualTotal() : LiveData<Int> {
        val currentDate = LocalDate.now()
        return itemDao.getTotalByRange(
            currentDate.with(TemporalAdjusters.firstDayOfYear()),
            currentDate.with(TemporalAdjusters.lastDayOfYear()))
            .asLiveData()
    }

    fun getItems() : LiveData<List<Item>> {
        var list = itemDao.getItems().asLiveData()
        return list
    }

    fun clearAllItems() : Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.deleteAllItems()
        }

        return true
    }
    private fun getNewItemEntry(drivenDate: LocalDate?, distance: Int) : Item {
        return Item(
            drivenDate = drivenDate,
            distance = distance
        )
    }

    fun addNewItem(distance: Int) {
        val newItem = getNewItemEntry(LocalDate.now(), distance)
        insertItem(newItem)
    }

    fun isEntryValid(distance: Int) : Boolean {
        if (distance == 0) {
            return false
        }
        return true
    }

//    private fun getCurrentDate() : String {
//        val current = LocalDate.now().plusDays(1)
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val formatted = current.format(formatter)
//        return formatted
//    }
}

class DistanceViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DistanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DistanceViewModel(itemDao) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }


}