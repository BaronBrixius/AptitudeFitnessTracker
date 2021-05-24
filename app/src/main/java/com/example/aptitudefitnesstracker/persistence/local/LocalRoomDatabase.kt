package com.example.aptitudefitnesstracker.persistence.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.ILocalDao
import com.example.aptitudefitnesstracker.application.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    version = 1,
    entities = [Routine::class, Exercise::class],
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
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
                    .fallbackToDestructiveMigration()
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

        suspend fun populateDatabase(routineDao: ILocalDao) {
            // test routines
            routineDao.deleteAllRoutines()
            routineDao.insert(Routine("Push"))
            routineDao.insert(Routine("Pull!"))
        }

    }

    // Migration Specs

//    @RenameColumn(tableName = "routines", fromColumnName = "id", toColumnName = "routineId")
//    class AutoMigrationSpec2To3 : AutoMigrationSpec
}
