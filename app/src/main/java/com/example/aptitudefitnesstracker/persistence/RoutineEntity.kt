package com.example.aptitudefitnesstracker.persistence

//import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    //@ColumnInfo(name = "id")
    var name: String
) {
    constructor(name: String) : this(0, name)
}