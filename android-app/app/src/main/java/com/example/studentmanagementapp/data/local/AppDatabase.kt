package com.example.studentmanagementapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.studentmanagementapp.model.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getDatabase(context.applicationContext).studentDao()
                                dao.insertStudent(
                                    Student(
                                        studentCode = "65010001",
                                        name = "Somchai",
                                        major = "Computer Science"
                                    )
                                )
                                dao.insertStudent(
                                    Student(
                                        studentCode = "65010002",
                                        name = "Suda",
                                        major = "Information Technology"
                                    )
                                )
                                dao.insertStudent(
                                    Student(
                                        studentCode = "65010003",
                                        name = "Mana",
                                        major = "Digital Technology"
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}