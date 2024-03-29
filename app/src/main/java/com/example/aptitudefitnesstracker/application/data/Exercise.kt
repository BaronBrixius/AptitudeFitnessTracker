package com.example.aptitudefitnesstracker.application.data

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
    @Exclude @set:Exclude @get:Exclude    //local id isn't needed for Firebase shares
    var id: Int,
    @Exclude @set:Exclude @get:Exclude    // JSON keeps track of the routine for each exercise so Firebase doesn't need this
    @ColumnInfo(name = "routineId", index = true)
    var routineId: Int,
    @Exclude @set:Exclude @get:Exclude    // users can't re-order online stuff so no need to track it, just default to alphabetical
    @ColumnInfo(name = "position", defaultValue = "99")
    var position: Int,
    @ColumnInfo(name = "name", defaultValue = "")
    var name: String,
    @ColumnInfo(name = "details", defaultValue = "")
    var details: ArrayList<Detail>,
    @ColumnInfo(name = "notes", defaultValue = "")
    var notes: String
) {
    constructor() : this(0, 0,99,"", ArrayList(),"")

    @Ignore
    constructor(name: String) : this(0, 0,99, name, ArrayList(), "")

    class Detail {
        var key: String = ""
        var value: Double = 0.0
    }
}