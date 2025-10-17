package com.example.holoai.ai

interface AiService {
    suspend fun complete(prompt: String): String
}

