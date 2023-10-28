package com.example.storyapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("password")
    val password: String,
) : Parcelable
