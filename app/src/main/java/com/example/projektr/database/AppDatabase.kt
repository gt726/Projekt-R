package com.example.projektr.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FinishedWorkout::class, FinishedWorkoutExercise::class, Template::class, TemplateExercise::class],
    version = 2
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun finishedWorkoutDao(): FinishedWorkoutDao
    abstract fun templateDao(): TemplateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // migracija baze s verzije 1 na verziju 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // stvori FinishedWorkout tablicu
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS FinishedWorkout (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                workoutName TEXT NOT NULL,
                date INTEGER NOT NULL
            )
        """
                )

                // stvori FinishedWorkoutExercise tablicu
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS FinishedWorkoutExercise (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                workoutId INTEGER NOT NULL,
                exerciseName TEXT NOT NULL,
                numberOfSets INTEGER NOT NULL,
                weights TEXT NOT NULL,
                reps TEXT NOT NULL,
                FOREIGN KEY(workoutId) REFERENCES FinishedWorkout(id) ON DELETE CASCADE
            )
        """
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "template_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
