package com.catinthedark.touchcontrols.pointer

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.catinthedark.touchcontrols.pointer.PointerGame

class PointerActivity : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cfg = AndroidApplicationConfiguration()
        initialize(PointerGame(), cfg)
    }
}
