package com.example.studentmanagementapp.data.local

import androidx.room.*
import com.example.studentmanagementapp.model.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    suspend fun getAllStudents(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    //ดึงข้อมูลจาก API มาเก็บ cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    //ลบข้อมูลทั้งหมดในตาราง
    @Query("DELETE FROM students")
    suspend fun deleteAll()
}