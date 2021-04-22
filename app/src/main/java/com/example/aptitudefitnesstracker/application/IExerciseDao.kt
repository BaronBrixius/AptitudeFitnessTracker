package com.example.aptitudefitnesstracker.application

interface IExerciseDao {
    fun getAllExercises(): List<Exercise>
}