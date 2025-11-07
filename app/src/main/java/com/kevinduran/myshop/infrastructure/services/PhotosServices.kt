package com.kevinduran.myshop.infrastructure.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.scale
import com.kevinduran.myshop.infrastructure.services.retrofit.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.max

class PhotosServices {

    companion object {
        private const val TAG = "PhotosService"
        private suspend fun compressImage(context: Context, image: Uri): ByteArray =
            withContext(Dispatchers.IO) {
                val input = context.contentResolver.openInputStream(image)
                val bitmap = BitmapFactory.decodeStream(input)
                input?.close()
                if (bitmap == null) return@withContext ByteArray(0)
                val maxSize = 1024f
                val scale = max(bitmap.width, bitmap.height) / maxSize
                val scaledBitmap = if (scale > 1) {
                    bitmap.scale((bitmap.width / scale).toInt(), (bitmap.height / scale).toInt())
                } else bitmap
                val output = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
                output.toByteArray()
            }

        suspend fun put(
            context: Context,
            uri: Uri,
            license: String,
            service: StorageService,
            type: String,
            fileName: String
        ): Result<Unit> {
            return withContext(Dispatchers.IO) {
                try {
                    val packageName = context.packageName
                    val bytes = compressImage(context, uri)
                    if (bytes.isEmpty()) {
                        throw Exception("Imagen invalida o corrupta, por favor intente con otra.")
                    }

                    val requestFile = bytes.toRequestBody("image/jpg".toMediaType())
                    val filePart = MultipartBody.Part.createFormData("file", fileName, requestFile)
                    val nameBody = fileName.toRequestBody("text/plain".toMediaType())

                    val response = when (type) {
                        "product" -> service.uploadProduct(license, packageName, nameBody, filePart)
                        "return" -> service.uploadReturn(license, packageName, nameBody, filePart)
                        "payment" -> service.uploadPayment(license, packageName, nameBody, filePart)
                        else -> throw IllegalArgumentException("Invalid type $type, internal error.")
                    }
                    if (!response.isSuccessful) throw Exception(response.message())
                    Result.success(Unit)
                } catch (_: IOException) {
                    Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }

    }

}