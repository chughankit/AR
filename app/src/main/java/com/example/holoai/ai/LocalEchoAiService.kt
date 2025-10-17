package com.example.holoai.ai

import kotlinx.coroutines.delay

class LocalEchoAiService : AiService {
    override suspend fun complete(prompt: String): String {
        delay(300) // simulate thinking
        return when {
            prompt.isBlank() -> "Hello! I’m here. What’s up?"
            else -> "You said: '${prompt}'. I’m your AR sidekick!"
        }
    }
}

