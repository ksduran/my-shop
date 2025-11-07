package com.kevinduran.myshop.ui.states

import java.io.File

sealed class ImageState {
    object Loading : ImageState()
    data class Success(val file: File) : ImageState()
    data class Error(val throwable: Throwable) : ImageState()
}
