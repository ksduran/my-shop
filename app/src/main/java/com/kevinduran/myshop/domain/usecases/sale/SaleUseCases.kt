package com.kevinduran.myshop.domain.usecases.sale

import javax.inject.Inject

class SaleUseCases @Inject constructor(
    val add: AddAllSalesUseCase,
    val delete: DeleteSaleUseCase,
    val updateSale: UpdateSaleUseCase,
    val registerSaleReturn: RegisterSaleReturnUseCase,
    val registerTransferPayment: RegisterTransferPaymentUseCase,
    val updateSalePaymentStatus: UpdateSalePaymentStatusUseCase,
    val assignUserToSales: AssignUserToSalesUseCase
)
