package com.example.studentmanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmanagementapp.data.repository.StudentRepository
import com.example.studentmanagementapp.model.Student
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    var students by mutableStateOf<List<Student>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isOnline by mutableStateOf(true)
        private set

    var searchQuery by mutableStateOf("")
        private set

    val filteredStudents: List<Student>
        get() = if (searchQuery.isBlank()) {
            students
        } else {
            students.filter {
                it.studentCode.contains(searchQuery, ignoreCase = true) ||
                        it.name.contains(searchQuery, ignoreCase = true) ||
                        it.major.contains(searchQuery, ignoreCase = true)
            }
        }

    fun loadStudents() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val result = repository.getStudents()
                students = result.students
                isOnline = result.isFromNetwork
            } catch (e: Exception) {
                errorMessage = "ไม่สามารถโหลดข้อมูลได้"
            } finally {
                isLoading = false
            }
        }
    }

    fun addStudent(studentCode: String, name: String, major: String) {
        viewModelScope.launch {
            repository.addStudent(
                Student(studentCode = studentCode, name = name, major = major)
            )
            students = repository.getLocalStudents()
        }
    }

    fun updateStudent(original: Student, studentCode: String, name: String, major: String) {
        viewModelScope.launch {
            repository.updateStudent(
                original.copy(studentCode = studentCode, name = name, major = major)
            )
            students = repository.getLocalStudents()
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            students = repository.getLocalStudents()
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }
}