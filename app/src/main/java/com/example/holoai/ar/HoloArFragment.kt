package com.example.holoai.ar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import com.gorisse.thomas.sceneform.light.LightEstimationConfig

/**
 * Custom AR fragment that downgrades light estimation to avoid calling
 * LightEstimate#acquireEnvironmentalHdrCubeMap() on devices with older ARCore builds.
 */
class HoloArFragment : ArFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setOnSessionConfigurationListener { _, config ->
            config.lightEstimationMode = Config.LightEstimationMode.AMBIENT_INTENSITY
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arSceneView.lightEstimationConfig = LightEstimationConfig(
            mode = Config.LightEstimationMode.AMBIENT_INTENSITY
        )
    }

    override fun onCreateSessionConfig(session: Session): Config {
        return super.onCreateSessionConfig(session).apply {
            lightEstimationMode = Config.LightEstimationMode.AMBIENT_INTENSITY
        }
    }
}
