package me.hirarki.pp.domain.repository

import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise

private external interface JsResponse : JsAny {
    val ok: Boolean
    val status: Int
    val statusText: String
    fun text(): Promise<JsString>
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
