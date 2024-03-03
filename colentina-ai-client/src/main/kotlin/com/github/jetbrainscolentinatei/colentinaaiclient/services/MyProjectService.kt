package com.github.jetbrainscolentinatei.colentinaaiclient.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Service(Service.Level.PROJECT)
class MyProjectService(val project: Project) {

    fun getRandomNumber() = (1..100).random()

    private suspend fun <T> withClient(block: suspend (HttpClient) -> T): T {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000
            }
        }.use {
            block(it)
        }
    }

    suspend fun getOpenFile(): String? = project.let {
        val editor = invokeOnEventThread {
            runCatching {
                FileEditorManager.getInstance(project).selectedTextEditor
            }.onFailure {
                thisLogger().warn(it)
            }.getOrNull()
        }

        val text = editor?.document?.text

        text
    }

    suspend fun chatError(code: String, error: String, task: String?): String {
        @Serializable
        data class Body(val code: String, val error: String, val task: String?)

        val response = withClient {
            it.post {
                url("http://localhost:5000/chat/error")
                contentType(ContentType.Application.Json)
                setBody(Body(code, error, task))
            }
        }

        if (response.status.value != 200) {
            throw Exception("Help: " + response.status.toString())
        }

        return response.body<String>()
    }

    suspend fun chatStyle(code: String, task: String?): String {
        @Serializable
        data class Body(val code: String, val task: String?)

        val response = withClient {
            it.post {
                url("http://localhost:5000/chat/style")
                contentType(ContentType.Application.Json)
                setBody(Body(code, task))
            }
        }

        if (response.status.value != 200) {
            throw Exception("Help: " + response.status.toString())
        }

        return response.body<String>()
    }

    suspend fun chatConcept(code: String?, concept: String, task: String?): String {
        @Serializable
        data class Body(val code: String?, val concept: String, val task: String?)

        val response = withClient {
            it.post {
                url("http://localhost:5000/chat/concept")
                contentType(ContentType.Application.Json)
                setBody(Body(code, concept, task))
            }
        }

        if (response.status.value != 200) {
            throw Exception("Help: " + response.status.toString())
        }

        return response.body<String>()
    }
}

suspend fun <T> invokeOnEventThread(action: () -> T): T {
    val app = ApplicationManager.getApplication()

    return invokeSuspendingOn(action, app::isDispatchThread, app::invokeLater)
}

suspend fun <T> invokeSuspendingOn(
    action: () -> T,
    executorNotRequired: () -> Boolean,
    executor: (Runnable) -> Unit
): T =
    if (executorNotRequired()) {
        action()
    } else {
        suspendCoroutine { continuation ->
            executor(Runnable { continuation.resumeWithAction(action) })
        }
    }

fun <T> Continuation<T>.resumeWithAction(action: () -> T) {
    try {
        val result = action()
        resume(result)
    } catch (e: Exception) {
        resumeWithException(e)
    }
}