package com.example.aptitudefitnesstracker.application.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Exclude    // local id isn't needed for Firebase shares
    var id: Int,
    @ColumnInfo(name = "position", defaultValue = "99")
    var position: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "tags", defaultValue = "")
    var tags: List<String>,
    @Ignore @Exclude    //convenience reference, don't want to persist it in either database like this
    var exercises: LiveData<List<Exercise>>
) {
    constructor() : this(0, 99,"", ArrayList(), MutableLiveData<List<Exercise>>())

    @Ignore
    constructor(name: String) : this(0,99, name, ArrayList(), MutableLiveData<List<Exercise>>())
}