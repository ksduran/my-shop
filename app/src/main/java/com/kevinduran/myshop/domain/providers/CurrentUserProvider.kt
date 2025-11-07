package com.kevinduran.myshop.domain.providers

interface CurrentUserProvider {
    fun getNameOrEmail(): String
}