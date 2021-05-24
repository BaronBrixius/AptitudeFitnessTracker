package com.example.aptitudefitnesstracker.application

import androidx.room.*

data class RoutineWithExercises(
    @Embedded
    val routine: Routine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "exerciseId"
    )
    val exercises: List<Exercise>
)