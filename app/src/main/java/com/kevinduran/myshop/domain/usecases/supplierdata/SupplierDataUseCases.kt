package com.kevinduran.myshop.domain.usecases.supplierdata

import javax.inject.Inject

class SupplierDataUseCases @Inject constructor(
    val addSupplierData: AddSupplierDataUseCase,
    val deleteSupplierData: DeleteSupplierDataUseCase,
    val updateSupplierData: UpdateSupplierDataUseCase
)
