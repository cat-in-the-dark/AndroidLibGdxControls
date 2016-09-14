package com.catinthedark.touchcontrols

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.ExtendViewport

class PointerGame : Game() {
    val scale = 32f
    val inv_scale = 1f / scale
    val vp_width = 1280 * inv_scale
    val vp_height = 720 * inv_scale

    lateinit var camera: OrthographicCamera
    lateinit var viewport: ExtendViewport
    lateinit var shapes: ShapeRenderer
    val playerPos = Vector3(0f, 0f, 0f)

    override fun create() {
        camera = OrthographicCamera()
        viewport = ExtendViewport(vp_width, vp_height, camera)
        shapes = ShapeRenderer()
        //Gdx.input.inputProcessor = this
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.projectionMatrix = camera.combined
        val pos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        camera.unproject(pos)
        playerPos.lerp(pos, Gdx.graphics.deltaTime)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color.WHITE
        shapes.circle(pos.x, pos.y, 0.25f, 16)
        shapes.color = Color.RED
        shapes.circle(playerPos.x, playerPos.y, 1f)
        shapes.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        shapes.dispose()
    }
}
