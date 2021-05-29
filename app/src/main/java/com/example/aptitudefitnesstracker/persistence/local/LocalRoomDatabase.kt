package com.example.aptitudefitnesstracker.persistence.local

import android.content.Context
import androidx.room.*
//import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.ILocalDao
import com.example.aptitudefitnesstracker.application.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    version = 3,
    entities = [Routine::class, Exercise::class],
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2, spec = LocalRoomDatabase.AutoMigrationSpec1To2::class),
//        AutoMigration(from = 2, to = 3)
//    ]
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
            // test routines
            localDao.insertRoutine(Routine("Push"))
            localDao.insertRoutine(Routine("Pull!"))
            val exercise = Exercise(0, 1,1, "Exercise", LinkedHashMap(), "")
            exercise.details["Foo"] = 42.0
            exercise.details["Oof"] = -37.5
            localDao.insertExercise(exercise)
        }
    }

    // Migration Specs
//    @DeleteColumn(tableName = "exercises", columnName = "tags") // cut the tags feature due to manpower, what a shame
//    class AutoMigrationSpec1To2 : AutoMigrationSpec
}
