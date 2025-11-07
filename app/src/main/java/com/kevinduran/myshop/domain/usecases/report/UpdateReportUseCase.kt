package com.kevinduran.myshop.domain.usecases.report

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Report
import com.kevinduran.myshop.domain.repositories.ReportsRepository
import java.time.Instant
import javax.inject.Inject

class UpdateReportUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke(name: String, entity: Report) {
        val updated = entity.copy(
            name = name.trim(),
            syncStatus = 0,
            updatedBy = Firebase.auth.currentUser?.displayName
                ?: Firebase.auth.currentUser?.email
                ?: "",
            updatedAt = Instant.now().toEpochMilli(),
        )
        repository.update(updated)
    }
}
