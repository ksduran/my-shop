package com.kevinduran.myshop.domain.usecases.report

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Report
import com.kevinduran.myshop.domain.repositories.ReportsRepository
import java.time.Instant
import javax.inject.Inject

class AddReportUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke(name: String) {
        val userName = Firebase.auth.currentUser?.displayName
            ?: Firebase.auth.currentUser?.email
            ?: ""
        val entity = Report(
            name = name.trim(),
            raisedBy = userName,
            updatedBy = userName,
            updatedAt = Instant.now().toEpochMilli(),
            createdAt = Instant.now().toEpochMilli(),
        )
        repository.add(entity)
    }
}
