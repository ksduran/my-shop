package com.kevinduran.myshop.domain.usecases.product

import javax.inject.Inject

class ProductUseCases @Inject constructor(
    val addProduct: AddProductUseCase,
    val deleteProduct: DeleteProductUseCase,
    val updateProduct: UpdateProductUseCase
)
