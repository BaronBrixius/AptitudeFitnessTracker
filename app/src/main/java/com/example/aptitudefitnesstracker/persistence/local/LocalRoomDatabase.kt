package com.example.aptitudefitnesstracker.persistence.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.aptitudefitnesstracker.application.RoutineDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [RoutineEntity::class], version = 1, exportSchema = false)
abstract class LocalRoomDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

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
                    populateDatabase(database.routineDao())
                }
            }
        }

        suspend fun populateDatabase(routineDao: RoutineDao) {
            // Delete all content here.
            routineDao.deleteAllRoutines()

            // sample routine names
            var routine = RoutineEntity("Push")
            routineDao.insert(routine)
            routine = RoutineEntity("Pull!")
            routineDao.insert(routine)

        }

    }
}
