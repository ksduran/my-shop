package com.kevinduran.myshop.domain.repositories

import com.kevinduran.myshop.domain.models.Report
import kotlinx.coroutines.flow.Flow

interface ReportsRepository {

    suspend fun add(entity: Report)

    suspend fun delete(entity: Report)

    fun get() : Flow<List<Report>>

    suspend fun getAllRemote()

    suspend fun update(entity: Report)

}