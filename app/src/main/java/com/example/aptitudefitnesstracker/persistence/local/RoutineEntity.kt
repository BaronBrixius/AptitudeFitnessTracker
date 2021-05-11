package com.example.aptitudefitnesstracker.persistence.local

//import androidx.room.ColumnInfo
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String
) {
    constructor(name: String) : this(0, name)
}