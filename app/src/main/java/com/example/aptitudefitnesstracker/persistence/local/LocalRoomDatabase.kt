package com.example.aptitudefitnesstracker.persistence.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aptitudefitnesstracker.application.dao.ILocalDao
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.data.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    version = 6,
    entities = [Routine::class, Exercise::class],
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class LocalRoomDatabase : RoomDatabase() {
    abstract fun iDao(): ILocalDao

    companion object {
        @Volatile
        private var INSTANCE: LocalRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): LocalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalRoomDatabase::class.java,
                    "routine"
                )
                    .addCallback(LocalRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class LocalRoomDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.iDao())
                }
            }
        }

        suspend fun populateDatabase(localDao: ILocalDao) {
            // example routines
            val exampleDetail = Exercise.Detail()
            exampleDetail.key = "Reps"
            exampleDetail.value = 5.0

            localDao.insertRoutine(Routine("Monday"))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 70.0
            localDao.insertExercise(Exercise(0, 1, 1, "Squats", arrayListOf(exampleDetail), ""))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 60.0
            localDao.insertExercise(Exercise(0, 1, 2, "Bench Press", arrayListOf(exampleDetail), ""))
            exampleDetail.key = "Reps"
            exampleDetail.value = 5.0
            localDao.insertExercise(Exercise(0, 1, 2, "Pull-ups", arrayListOf(exampleDetail), ""))

            localDao.insertRoutine(Routine("Wednesday"))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 60.0
            localDao.insertExercise(Exercise(0, 2, 1, "Deadlift", arrayListOf(exampleDetail), ""))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 20.0
            localDao.insertExercise(Exercise(0, 2, 2, "Overhead Press", arrayListOf(exampleDetail), ""))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 40.0
            localDao.insertExercise(Exercise(0, 2, 2, "Inverted Rows", arrayListOf(exampleDetail), ""))

            localDao.insertRoutine(Routine("Friday"))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 60.0
            localDao.insertExercise(Exercise(0, 3, 1, "Bench Press", arrayListOf(exampleDetail), ""))
            exampleDetail.key = "Kilos"
            exampleDetail.value = 70.0
            localDao.insertExercise(Exercise(0, 3, 2, "Squats", arrayListOf(exampleDetail), ""))
            exampleDetail.key = "Reps"
            exampleDetail.value = 5.0
            localDao.insertExercise(Exercise(0, 3, 2, "Dips", arrayListOf(exampleDetail), ""))
        }
    }
}
