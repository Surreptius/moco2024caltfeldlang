package edu.moco.pomtodoro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.room.Room
import edu.moco.pomtodoro.data.Database
import edu.moco.pomtodoro.data.todo.TodoRepository
import edu.moco.pomtodoro.logic.MainViewModel
import edu.moco.pomtodoro.ui.screens.Navigation
import edu.moco.pomtodoro.ui.theme.ApplicationTheme

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room
            .databaseBuilder(applicationContext, Database::class.java, "todo-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel(repository = TodoRepository(db.todoDao()))

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                64
            )
        }

        setContent {
            ApplicationTheme {
                Navigation(viewModel = viewModel)
            }
        }
    }
}