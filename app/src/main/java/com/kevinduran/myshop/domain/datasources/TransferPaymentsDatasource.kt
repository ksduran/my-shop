package com.kevinduran.myshop.domain.datasources

import com.kevinduran.myshop.domain.models.TransferPayment

interface TransferPaymentsDatasource {
    suspend fun add(entity: TransferPayment): Result<Unit>
    suspend fun addAll(transferPayments: List<TransferPayment>): Result<Unit>
    suspend fun delete(entity: TransferPayment): Result<Unit>
    suspend fun delete(payments: List<TransferPayment>): Result<Unit>
    suspend fun getAll(): Result<List<TransferPayment>>
    suspend fun update(entity: TransferPayment): Result<Unit>
}