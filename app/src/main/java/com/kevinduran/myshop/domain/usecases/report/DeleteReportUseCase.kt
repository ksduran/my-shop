package com.kevinduran.myshop.domain.usecases.report

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Report
import com.kevinduran.myshop.domain.repositories.ReportsRepository
import java.time.Instant
import javax.inject.Inject

class DeleteReportUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke(entity: Report) {
        val updated = entity.copy(
            deleted = 1,
            syncStatus = 0,
            updatedBy = Firebase.auth.currentUser?.displayName
                ?: Firebase.auth.currentUser?.email
                ?: "",
            updatedAt = Instant.now().toEpochMilli(),
        )
        repository.delete(updated)
    }
}
