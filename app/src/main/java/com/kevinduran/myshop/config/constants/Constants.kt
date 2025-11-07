package com.kevinduran.myshop.config.constants

sealed class Payment(val status: String) {

    data object All : Payment("%")
    data object ByRaised : Payment("Por cobrar")
    data object PaidByTransfer : Payment("Pago Transferencia")
    data object PaidByCash : Payment("Pago Efectivo")
    data object PaidInLocal : Payment("Pago en local")
    data object Pending : Payment("Pendiente")
    data object Return : Payment("Devolución")
    data object Change : Payment("Cambio")

}