package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleEmployeeAccessUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: EmployeesRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `toggle access employee`() = runTest {
        val fixedUser = "testUser"
        val fixedNow = 1_700_000_000_000L

        val currentUserProvider = object : CurrentUserProvider {
            override fun getNameOrEmail(): String = fixedUser
        }
        val timerProvider = object : TimerProvider {
            override fun getNowMillis(): Long = fixedNow
        }

        coEvery { repository.update(any()) } throws Exception("DB error")

        val useCase = ToggleEmployeeAccessUseCase(
            repository = repository,
            dispatcher = StandardTestDispatcher(testScheduler),
            currentUserProvider = currentUserProvider,
            timerProvider = timerProvider
        )
        val result = useCase(Employee(status = 1))

        assertTrue(result.isFailure)
        coVerify(exactly = 1) { repository.update(match { it.status == 0 }) }
    }


}