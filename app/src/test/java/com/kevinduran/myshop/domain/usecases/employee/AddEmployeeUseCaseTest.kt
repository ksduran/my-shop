package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.domain.models.EmployeePermissions
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.providers.UUIDProvider
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

class AddEmployeeUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: EmployeesRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `add new employee to database - quick`() = runTest {

        val fixedUUID = "uuid-test-1"
        val fixedNow = 1_700_000_000_000L
        val currentUser = "testUser"
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val uuidProvider = object : UUIDProvider {
            override fun randomUUID(): String = fixedUUID
        }
        val timerProvider = object : TimerProvider {
            override fun getNowMillis(): Long = fixedNow
        }
        val currentUserProvider = object : CurrentUserProvider {
            override fun getNameOrEmail(): String = currentUser
        }

        coEvery { repository.add(any()) } just Runs

        val useCase = AddEmployeeUseCase(
            repository = repository,
            currentUserProvider = currentUserProvider,
            timerProvider = timerProvider,
            uuidProvider = uuidProvider,
            ioDispatcher = testDispatcher
        )

        val result = useCase("Luis", "@user", EmployeePermissions())
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            repository.add(match {
                it.name == "Luis"
                        && it.user == "@user"
                        && it.uuid == fixedUUID
                        && it.raisedBy == currentUser
                        && it.createdAt == fixedNow
                        && it.updatedAt == fixedNow
            })
        }
    }

}