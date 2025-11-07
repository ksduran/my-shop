package com.kevinduran.myshop.domain.datasources

import com.kevinduran.myshop.domain.models.Report
import kotlinx.coroutines.flow.Flow

interface ReportsDatasource {

    suspend fun add(entity: Report)

    suspend fun delete(entity: Report)

    fun get() : Flow<List<Report>>

    suspend fun getAllRemote()

    suspend fun update(entity: Report)

}