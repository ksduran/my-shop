package com.kevinduran.myshop.domain.usecases.product

import com.kevinduran.myshop.domain.repositories.ProductsRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class AddProductUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ProductsRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `add product to room`() {

    }

}