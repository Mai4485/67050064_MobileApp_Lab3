package com.example.studentmanagementapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("student_code")
    val studentCode: String,
    val name: String,
    val major: String
)