package com.catinthedark.touchcontrols.knob

import android.util.Log
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ExtendViewport

/**
 * Created by kirill on 15.09.16.
 */

class KnobGame : Game() {
    val scale = 32f
    val inv_scale = 1f / scale
    val vp_width = 1280 * inv_scale
    val vp_height = 720 * inv_scale
    val speed = 0.3f

    lateinit var camera: OrthographicCamera
    lateinit var viewport: ExtendViewport
    lateinit var shapes: ShapeRenderer
    lateinit var batch: SpriteBatch
    lateinit var stage: Stage
    lateinit var touchpad: Touchpad

    val playerPos = Vector3(0f, 0f, 0f)

    override fun create() {
        camera = OrthographicCamera()
        viewport = ExtendViewport(vp_width, vp_height, camera)
        shapes = ShapeRenderer()
        batch = SpriteBatch()
        val touchpadStyle = Touchpad.TouchpadStyle()
        touchpadStyle.background = TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("textures/touchBackground.png"))))
        touchpadStyle.knob = TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("textures/touchKnob.png"))))
        touchpad = Touchpad(10f, touchpadStyle)
        touchpad.setBounds(15f, 15f, 250f, 250f)
        stage = Stage()
        stage.addActor(touchpad)
        Gdx.input.inputProcessor = stage
        //Gdx.input.inputProcessor = this
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.projectionMatrix = camera.combined
        val pos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        camera.unproject(pos)
        playerPos.add(touchpad.knobPercentX * speed, touchpad.knobPercentY * speed, 0f)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color.RED
        shapes.circle(playerPos.x, playerPos.y, 1f)
        shapes.end()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        stage.viewport.update(width, height, true);
    }

    override fun dispose() {
        shapes.dispose()
        stage.dispose()
    }
}
