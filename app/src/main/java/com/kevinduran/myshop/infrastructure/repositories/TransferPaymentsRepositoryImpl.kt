package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.TransferPaymentsDatasource
import com.kevinduran.myshop.domain.models.TransferPayment
import com.kevinduran.myshop.domain.repositories.TransferPaymentsRepository
import javax.inject.Inject

class TransferPaymentsRepositoryImpl @Inject constructor(
    private val datasource: TransferPaymentsDatasource
) : TransferPaymentsRepository {

    override suspend fun add(entity: TransferPayment): Result<Unit> = datasource.add(entity)

    override suspend fun addAll(transferPayments: List<TransferPayment>): Result<Unit> = datasource.addAll(transferPayments)

    override suspend fun delete(entity: TransferPayment): Result<Unit> = datasource.delete(entity)

    override suspend fun delete(payments: List<TransferPayment>): Result<Unit> = datasource.delete(payments)

    override suspend fun getAll(): Result<List<TransferPayment>> = datasource.getAll()

    override suspend fun update(entity: TransferPayment): Result<Unit> = datasource.update(entity)
}

