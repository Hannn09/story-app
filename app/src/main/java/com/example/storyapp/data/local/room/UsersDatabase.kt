package com.example.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.data.local.entity.UsersEntity

@Database(entities = [UsersEntity::class], version = 1, exportSchema = false)
abstract class UsersDatabase:  RoomDatabase() {
    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var instance: UsersDatabase ? = null
        fun getInstance(context: Context): UsersDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UsersDatabase::class.java, "Users.db"
                ).build()
            }
    }
}