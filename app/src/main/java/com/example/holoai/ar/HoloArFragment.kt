package com.example.holoai.ar

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

/**
 * Custom AR fragment that downgrades light estimation to avoid calling
 * LightEstimate#acquireEnvironmentalHdrCubeMap() on devices with older ARCore builds.
 */
class HoloArFragment : ArFragment() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setOnSessionConfigurationListener { _, config ->
            config.lightEstimationMode = Config.LightEstimationMode.AMBIENT_INTENSITY
        }
    }

    override fun onCreateSessionConfig(session: Session): Config {
        return super.onCreateSessionConfig(session).apply {
            lightEstimationMode = Config.LightEstimationMode.AMBIENT_INTENSITY
        }
    }
}
