package com.catinthedark.touchcontrols.pointer

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.catinthedark.touchcontrols.knob.KnobGame

class KnobActivity : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cfg = AndroidApplicationConfiguration()
        initialize(KnobGame(), cfg)
    }
}
