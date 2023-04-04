package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Query("SELECT id, date, distance FROM Item ORDER BY date")
    fun getItems() : Flow<List<Item>>

    @Query("SELECT SUM(distance) FROM Item WHERE date = :selectedDate")
    fun getTotalByDate(selectedDate: LocalDate) : Flow<Int>

    @Query("SELECT SUM(distance) FROM Item WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalByRange(startDate: LocalDate, endDate: LocalDate) : Flow<Int>

    @Query("DELETE FROM Item")
    fun deleteAllItems()

    @Query("DELETE FROM Item WHERE id = :itemId")
    fun deleteItemById(itemId: Int)
}