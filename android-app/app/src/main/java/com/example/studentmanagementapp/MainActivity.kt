package com.example.studentmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentmanagementapp.data.local.AppDatabase
import com.example.studentmanagementapp.data.remote.RetrofitClient
import com.example.studentmanagementapp.data.repository.StudentRepository
import com.example.studentmanagementapp.ui.AddEditStudentScreen
import com.example.studentmanagementapp.ui.StudentDetailScreen
import com.example.studentmanagementapp.ui.StudentListScreen
import com.example.studentmanagementapp.ui.theme.StudentManagementAppTheme
import com.example.studentmanagementapp.viewmodel.StudentViewModel
import com.example.studentmanagementapp.viewmodel.StudentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ประกอบ Dependency ด้วยมือ (ไม่ใช้ Hilt/Koin ตามขอบเขตของใบงาน Part 1)
        val dao = AppDatabase.getDatabase(applicationContext).studentDao()
        val repository = StudentRepository(dao, RetrofitClient.api)
        val factory = StudentViewModelFactory(repository)

        setContent {
            StudentManagementAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: StudentViewModel = viewModel(factory = factory)
                    StudentManagementApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun StudentManagementApp(viewModel: StudentViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            StudentListScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("add") },
                onStudentClick = { student -> navController.navigate("detail/${student.id}") }
            )
        }

        composable("add") {
            AddEditStudentScreen(
                onSave = { code, name, major ->
                    viewModel.addStudent(code, name, major)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = "detail/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val student = viewModel.students.find { it.id == studentId }

            if (student != null) {
                StudentDetailScreen(
                    student = student,
                    onEdit = { navController.navigate("edit/${student.id}") },
                    onDelete = {
                        viewModel.deleteStudent(student)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "edit/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val student = viewModel.students.find { it.id == studentId }

            if (student != null) {
                AddEditStudentScreen(
                    existingStudent = student,
                    onSave = { code, name, major ->
                        viewModel.updateStudent(student, code, name, major)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}