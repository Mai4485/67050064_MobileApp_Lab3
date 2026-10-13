package com.example.studentmanagementapp.data.remote

import com.example.studentmanagementapp.model.Student
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentApi {
    @GET("students")
    suspend fun getStudents(): List<Student>

    @POST("students")
    suspend fun createStudent(@Body student: Student): Student

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id: Int, @Body student: Student): Student

    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") id: Int)
}