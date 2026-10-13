package com.example.studentmanagementapp.data.repository

import com.example.studentmanagementapp.data.local.StudentDao
import com.example.studentmanagementapp.data.remote.StudentApi
import com.example.studentmanagementapp.model.Student

data class StudentResult(val students: List<Student>, val isFromNetwork: Boolean)

class StudentRepository(
    private val dao: StudentDao,
    private val api: StudentApi
) {
    suspend fun getStudents(): StudentResult {
        return try {
            val remoteStudents = api.getStudents()
            dao.deleteAll()
            dao.insertAll(remoteStudents)
            StudentResult(remoteStudents, isFromNetwork = true)
        } catch (e: Exception) {
            android.util.Log.e("StudentRepository", "API call failed", e)
            StudentResult(dao.getAllStudents(), isFromNetwork = false)
        }
    }

    suspend fun getLocalStudents(): List<Student> {
        return dao.getAllStudents()
    }

    suspend fun addStudent(student: Student) {
        try {
            val created = api.createStudent(student)
            dao.insertStudent(created)
        } catch (e: Exception) {
            dao.insertStudent(student)
        }
    }

    suspend fun updateStudent(student: Student) {
        try {
            val updated = api.updateStudent(student.id, student)
            dao.updateStudent(updated)
        } catch (e: Exception) {
            dao.updateStudent(student)
        }
    }

    suspend fun deleteStudent(student: Student) {
        try {
            api.deleteStudent(student.id)
            dao.deleteStudent(student)
        } catch (e: Exception) {
            dao.deleteStudent(student)
        }
    }
}