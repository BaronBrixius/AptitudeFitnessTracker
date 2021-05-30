package com.example.aptitudefitnesstracker.persistence.local

import android.content.Context
import androidx.room.*
//import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.dao.ILocalDao
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

    private class LocalRoomDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
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
            localDao.insertExercise(Exercise(0, 0,1, "Squats", arrayListOf(exampleDetail), ""))
            localDao.insertExercise(Exercise(0, 0,2, "Bench Press", arrayListOf(exampleDetail), ""))
            localDao.insertExercise(Exercise(0, 0,2, "Pull-ups", arrayListOf(exampleDetail), ""))

            localDao.insertRoutine(Routine("Wednesday"))
            localDao.insertExercise(Exercise(0, 0,1, "Deadlift", arrayListOf(exampleDetail), ""))
            localDao.insertExercise(Exercise(0, 0,2, "Overhead Press", arrayListOf(exampleDetail), ""))
            localDao.insertExercise(Exercise(0, 0,2, "Inverted Rows", arrayListOf(exampleDetail), ""))

            localDao.insertRoutine(Routine("Friday"))
            localDao.insertExercise(Exercise(0, 0,1, "Bench Press", arrayListOf(exampleDetail), ""))
            localDao.insertExercise(Exercise(0, 0,2, "Squats", arrayListOf(exampleDetail), ""))
            localDao.insertExercise(Exercise(0, 0,2, "Dips", arrayListOf(exampleDetail), ""))
        }
    }
}
