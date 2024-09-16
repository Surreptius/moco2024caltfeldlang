package edu.moco.pomtodoro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.moco.pomtodoro.data.todo.TodoDao
import edu.moco.pomtodoro.data.todo.TodoItem

@Database(entities = [TodoItem::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}