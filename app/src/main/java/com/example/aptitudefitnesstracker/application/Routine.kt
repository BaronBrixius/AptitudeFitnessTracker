package com.example.aptitudefitnesstracker.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

//@IgnoreExtraProperties
@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Exclude    //local id isn't needed for Firebase shares
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "tags", defaultValue = "")
    var tags: List<String>
) {

    constructor() : this(0,"",ArrayList())
    constructor(name: String) : this(0,name,ArrayList())
    constructor(name: String, tags: List<String>) : this(0, name, tags)
}