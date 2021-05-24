package com.example.aptitudefitnesstracker.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

//@IgnoreExtraProperties
@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "routineId")
    @Exclude    // local id isn't needed for Firebase shares
    val id: Long,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "tags", defaultValue = "")
    var tags: List<String>
) {
    constructor() : this(0, "", ArrayList())
    @Ignore
    constructor(name: String) : this(0, name, ArrayList())
}