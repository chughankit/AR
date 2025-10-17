package com.example.holoai.ar

import com.gorisse.thomas.sceneform.math.Position
import com.gorisse.thomas.sceneform.math.Rotation
import com.gorisse.thomas.sceneform.rendering.ModelRenderable
import com.gorisse.thomas.sceneform.ux.ArFragment
import com.gorisse.thomas.sceneform.Node

class AvatarController(
    private val arFragment: ArFragment,
    private val renderable: ModelRenderable
) {
    private var node: Node? = null

    init {
        placeAtLastHit()
    }

    private fun placeAtLastHit() {
        val frame = arFragment.arSceneView.arFrame ?: return
        val hits = frame.hitTestCenter(arFragment.arSceneView)
        val pose = hits.firstOrNull()?.hitPose ?: return

        val anchor = arFragment.arSceneView.session?.createAnchor(pose) ?: return
        node = Node().apply {
            setParent(arFragment.arSceneView.scene)
            setAnchor(anchor)
            setRenderable(renderable)
            localPosition = Position(0f, 0f, 0f)
            localRotation = Rotation(0f, 0f, 0f)
        }
    }

    fun emoteSpeaking() {
        // Placeholder: simple nudge to indicate speaking
        node?.let {
            val y = it.localPosition.y
            it.localPosition = Position(0f, y + 0.02f, 0f)
        }
    }
}

private fun com.google.ar.core.Frame.hitTestCenter(view: com.gorisse.thomas.sceneform.ArSceneView): List<com.gorisse.thomas.sceneform.HitResult> {
    val cx = view.width / 2f
    val cy = view.height / 2f
    return this.hitTest(cx, cy)
}

