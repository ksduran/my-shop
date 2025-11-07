package com.kevinduran.myshop.domain.usecases.employee

import javax.inject.Inject

class EmployeeUseCases @Inject constructor(
    val addEmployee: AddEmployeeUseCase,
    val deleteEmployees: DeleteEmployeesUseCase,
    val getAll: GetAllEmployeesUseCase,
    val updateEmployee: UpdateEmployeeUseCase,
    val toggleEmployeeAccess: ToggleEmployeeAccessUseCase
)
