package com.kevinduran.myshop.infrastructure.local.entities

import androidx.room.Entity

@Entity(tableName = "employees")
data class EmployeeEntity(
    val id: String,
    val name: String,
    val user: String,
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)