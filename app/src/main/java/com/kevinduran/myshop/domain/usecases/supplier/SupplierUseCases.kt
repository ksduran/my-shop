package com.kevinduran.myshop.domain.usecases.supplier

import javax.inject.Inject

class SupplierUseCases @Inject constructor(
    val addSupplier: AddSupplierUseCase,
    val deleteSupplier: DeleteSupplierUseCase,
    val updateSupplier: UpdateSupplierUseCase
)
