package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.models.EmployeePermissions
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

class UpdateEmployeeUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: EmployeesRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `update employee and validate exceptions`() = runTest {

        val fixedUser = "testUser"
        val fixedNow = 1_700_000_000_000L

        val currentUserProvider = object : CurrentUserProvider {
            override fun getNameOrEmail(): String = fixedUser
        }

        val timerProvider = object : TimerProvider {
            override fun getNowMillis(): Long = fixedNow
        }

        coEvery { repository.update(any()) } just Runs

        val useCase = UpdateEmployeeUseCase(
            repository = repository,
            currentUserProvider = currentUserProvider,
            timerProvider = timerProvider,
            ioDispatcher = StandardTestDispatcher(testScheduler)
        )
        val result = useCase(
            Employee(),
            name = "Mario",
            user = "@mario",
            permissions = EmployeePermissions()
        )

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            repository.update(match {
                it.name == "Mario"
                        && it.user == "@mario"
            })
        }

    }

}