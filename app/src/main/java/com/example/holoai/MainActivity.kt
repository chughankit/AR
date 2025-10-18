package com.example.holoai

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.example.holoai.ar.AvatarController
import com.example.holoai.voice.VoiceManager
import com.example.holoai.ai.LocalEchoAiService
import com.google.ar.core.Anchor
import android.view.MotionEvent
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    private lateinit var status: TextView
    private lateinit var btnPlace: Button
    private lateinit var btnTalk: Button

    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    private var avatarController: AvatarController? = null
    private lateinit var voice: VoiceManager

    private val ai = LocalEchoAiService()

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    private val requestPerms = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = results.values.all { it }
        status.text = if (granted) "Permissions granted" else "Permissions needed for AR + Voice"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        status = findViewById(R.id.txtStatus)
        btnPlace = findViewById(R.id.btnPlace)
        btnTalk = findViewById(R.id.btnTalk)

        voice = VoiceManager(this)

        ensurePermissions()

        btnPlace.setOnClickListener {
            status.text = "Tap on a plane to place"
            setupTapToPlace()
        }

        btnTalk.setOnClickListener {
            startVoiceChat()
        }
    }

    private fun ensurePermissions() {
        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) {
            requestPerms.launch(missing.toTypedArray())
        }
    }

    private fun setupTapToPlace() {
        arFragment.setOnTapPlaneGlb(
            glbAsset = "models/companion.glb",
            onPlaced = { renderable, anchor ->
                avatarController = AvatarController(arFragment, renderable).also { ctrl ->
                    ctrl.placeAtAnchor(anchor)
                    status.text = "Companion placed. Say something!"
                }
            },
            onError = { err -> status.text = "Model error: ${'$'}err" }
        )
    }

    private fun startVoiceChat() {
        status.text = "Listening…"
        voice.listen(
            onResult = { text ->
                status.text = "You: ${'$'}text\nThinking…"
                uiScope.launch {
                    val reply = ai.complete(text)
                    status.text = "Companion: ${'$'}reply"
                    voice.speak(reply)
                    avatarController?.emoteSpeaking()
                }
            },
            onError = { e -> status.text = "Voice error: ${'$'}e" }
        )
    }
}

// Extension helper to keep MainActivity clean
private fun ArFragment.setOnTapPlaneGlb(
    glbAsset: String,
    onPlaced: (ModelRenderable, Anchor) -> Unit,
    onError: (String) -> Unit
) {
    setOnTapArPlaneListener { hitResult: HitResult, _: Plane, _: MotionEvent ->
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(glbAsset))
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { renderable ->
                val anchor = hitResult.createAnchor()
                onPlaced(renderable, anchor)
            }
            .exceptionally { throwable: Throwable -> onError(throwable.message ?: "Unknown error"); null }
    }
}
