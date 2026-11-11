package me.hirarki.pp.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.jetbrains.skia.Image
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise

private external interface JsResponse : JsAny {
    val ok: Boolean
    val status: Int
    val statusText: String
    fun text(): Promise<JsString>
}

private external interface JsImageResponse : JsAny {
    val ok: Boolean
    val status: Int
    val statusText: String
    fun arrayBuffer(): Promise<ArrayBuffer>
}

private external interface JsByteView : JsAny {
    val length: Int
    operator fun get(index: Int): Byte
}

actual suspend fun fetchContentJson(filename: String): String {
    val url = "/content/$filename"
    
    val response = suspendCancellableCoroutine<JsResponse> { continuation ->
        window.fetch(url).then(
            onFulfilled = { value ->
                continuation.resume(value.unsafeCast<JsResponse>())
                null
            },
            onRejected = { error ->
                continuation.resumeWithException(Exception("Fetch failed: $error"))
                null
            }
        )
    }
    
    if (!response.ok) {
        throw Exception("Failed to fetch $filename: ${response.status} ${response.statusText}")
    }
    
    return suspendCancellableCoroutine<String> { continuation ->
        response.text().then(
            onFulfilled = { value ->
                continuation.resume(value.toString())
                null
            },
            onRejected = { error ->
                continuation.resumeWithException(Exception("Text extraction failed: $error"))
                null
            }
        )
    }
}

actual suspend fun loadProjectImage(imagePath: String): ImageBitmap {
    require(imagePath.startsWith("/images/projects/")) {
        "Project image path must use /images/projects/"
    }

    val response = suspendCancellableCoroutine<JsImageResponse> { continuation ->
        window.fetch(imagePath).then(
            onFulfilled = { value ->
                continuation.resume(value.unsafeCast<JsImageResponse>())
                null
            },
            onRejected = { error ->
                continuation.resumeWithException(Exception("Image fetch failed: $error"))
                null
            }
        )
    }

    if (!response.ok) {
        throw Exception("Failed to fetch project image: ${response.status} ${response.statusText}")
    }

    val buffer = suspendCancellableCoroutine<ArrayBuffer> { continuation ->
        response.arrayBuffer().then(
            onFulfilled = { value ->
                continuation.resume(value)
                null
            },
            onRejected = { error ->
                continuation.resumeWithException(Exception("Image data extraction failed: $error"))
                null
            }
        )
    }
    val byteView = Uint8Array(buffer).unsafeCast<JsByteView>()
    val bytes = ByteArray(byteView.length) { index -> byteView[index] }
    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}
