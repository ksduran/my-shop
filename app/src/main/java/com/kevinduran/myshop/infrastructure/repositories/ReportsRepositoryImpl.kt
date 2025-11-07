package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.ReportsDatasource
import com.kevinduran.myshop.domain.models.Report
import com.kevinduran.myshop.domain.repositories.ReportsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val datasource: ReportsDatasource
) : ReportsRepository {

    override suspend fun add(entity: Report) {
        datasource.add(entity)
    }

    override suspend fun delete(entity: Report) {
        datasource.delete(entity)
    }

    override fun get(): Flow<List<Report>> {
        return datasource.get()
    }

    override suspend fun getAllRemote() = datasource.getAllRemote()

    override suspend fun update(entity: Report) {
        datasource.update(entity)
    }
}