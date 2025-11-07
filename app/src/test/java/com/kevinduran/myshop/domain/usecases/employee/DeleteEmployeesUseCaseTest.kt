package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteEmployeesUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: EmployeesRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `delete employee and validate exceptions`() = runTest {

        val fixedNow = 1_700_000_000_000L
        val currentUser = "testUser"

        val timerProvider = object : TimerProvider {
            override fun getNowMillis(): Long = fixedNow
        }

        val currentUserProvider = object : CurrentUserProvider {
            override fun getNameOrEmail(): String = currentUser
        }

        coEvery { repository.update(any()) } just Runs

        val useCase = DeleteEmployeesUseCase(
            repository = repository,
            ioDispatcher = StandardTestDispatcher(testScheduler),
            timerProvider = timerProvider,
            currentUserProvider = currentUserProvider
        )
        val employeesList = listOf(Employee(), Employee())
        val result = useCase(employeesList)

        assertTrue(result.isSuccess)
        coVerify(exactly = employeesList.size) { repository.update(any()) }

    }

}