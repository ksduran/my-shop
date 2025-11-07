package com.kevinduran.myshop.domain.usecases.report

import javax.inject.Inject

class ReportUseCases @Inject constructor(
    val addReport: AddReportUseCase,
    val deleteReport: DeleteReportUseCase,
    val updateReport: UpdateReportUseCase
)
