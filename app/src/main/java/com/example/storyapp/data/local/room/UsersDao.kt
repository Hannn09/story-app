package com.example.storyapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun loginUser(email: String, password: String): LiveData<UsersEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(users: UsersEntity)
}