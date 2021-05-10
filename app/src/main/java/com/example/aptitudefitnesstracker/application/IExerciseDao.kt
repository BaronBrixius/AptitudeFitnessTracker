package com.example.aptitudefitnesstracker.application

interface IExerciseDao {
//    fun getExercise(id: Int): Exercise
    fun getAllExercises(): List<Exercise>
}