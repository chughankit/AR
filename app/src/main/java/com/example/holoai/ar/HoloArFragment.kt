package com.example.holoai.ar

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

/**
 * Custom AR fragment that disables environmental HDR lighting. Some devices ship older
 * ARCore builds that are missing LightEstimate#acquireEnvironmentalHdrCubeMap(), which
 * Sceneform tries to call when HDR is enabled. For compatibility we downgrade to the
 * ambient intensity mode.
 */
class HoloArFragment : ArFragment() {
    override fun getSessionConfiguration(session: Session): Config {
        val config = super.getSessionConfiguration(session)
        config.lightEstimationMode = Config.LightEstimationMode.AMBIENT_INTENSITY
        session.configure(config)
        return config
    }
}

