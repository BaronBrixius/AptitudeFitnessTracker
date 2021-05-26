package com.example.aptitudefitnesstracker.application

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.firebase.database.Exclude

@Entity(
    tableName = "exercises",
    foreignKeys = [ForeignKey(
        entity = Routine::class,
        parentColumns = ["id"],
        childColumns = ["routineId"],
        onDelete = CASCADE
    )]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Exclude    //local id isn't needed for Firebase shares
    val id: Int,
    @ColumnInfo(name = "routineId", index = true)
    val routineId: Int,
    @ColumnInfo(name = "name", defaultValue = "")
    var name: String,
    @ColumnInfo(name = "tags", defaultValue = "")
    var tags: List<String>,
    @ColumnInfo(name = "details", defaultValue = "")
    var details: LinkedHashMap<String, Double>,
    @ColumnInfo(name = "notes", defaultValue = "")
    var notes: String
) {
    constructor() : this(0, 0,"", ArrayList(),LinkedHashMap(),"")

//    @Ignore
//    constructor(name: String) : this(0, 0, name, ArrayList())
//
//    @Ignore
//    constructor(name: String, tags: List<String>, exercises: List<Exercise>) : this(0, 0, name, tags)
}