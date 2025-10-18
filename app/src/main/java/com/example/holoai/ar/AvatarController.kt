package com.example.holoai.ar

import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment

class AvatarController(
    private val arFragment: ArFragment,
    private val renderable: ModelRenderable
) {
    private var node: Node? = null

    fun placeAtAnchor(anchor: Anchor) {
        val anchorNode = AnchorNode(anchor).apply {
            setParent(arFragment.arSceneView.scene)
        }
        node = Node().apply {
            setParent(anchorNode)
            setRenderable(renderable)
            localPosition = Vector3(0f, 0f, 0f)
        }
    }

    fun emoteSpeaking() {
        node?.let {
            val y = it.localPosition.y
            it.localPosition = Vector3(0f, y + 0.02f, 0f)
        }
    }
}
