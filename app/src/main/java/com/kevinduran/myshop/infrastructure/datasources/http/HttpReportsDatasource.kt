package com.kevinduran.myshop.infrastructure.datasources.http

import com.kevinduran.myshop.domain.datasources.ReportsDatasource
import com.kevinduran.myshop.domain.models.Report
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class HttpReportsDatasource @Inject constructor() : ReportsDatasource {
    override suspend fun add(entity: Report) {

    }

    override suspend fun delete(entity: Report) {

    }

    override fun get(): Flow<List<Report>> {
        return emptyFlow()
    }

    override suspend fun getAllRemote() {

    }

    override suspend fun update(entity: Report) {

    }
}