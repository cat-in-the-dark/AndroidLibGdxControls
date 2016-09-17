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
import com.catinthedark.touchcontrols.Intervals
import java.util.concurrent.TimeUnit

/**
 * Created by kirill on 15.09.16.
 */

class KnobGame : Game() {
    val scale = 32f
    val inv_scale = 1f / scale
    val vp_width = 1280 * inv_scale
    val vp_height = 720 * inv_scale
    val speed = 0.3f
    val minAimMultiplier = 5f
    val aimMultiplierSpeed = 10f
    val maxAimMultiplier = 20f
    val playerPos = Vector3(0f, 0f, 0f)
    val targetCenterPos = Vector3(20f, 25f, 0f)
    val targetPos = Vector3(5f, 25f, 0f)
    val angularSpeed = 2.5f
    val bulletPos = Vector3(0f, 0f, 0f)

    lateinit var camera: OrthographicCamera
    lateinit var viewport: ExtendViewport
    lateinit var shapes: ShapeRenderer
    lateinit var batch: SpriteBatch
    lateinit var stage: Stage
    lateinit var moveTouchPad: Touchpad
    lateinit var aimTouchPad: Touchpad

    var aimMultiplier = minAimMultiplier
    var aimVector = Vector3(0f, 0f, 0f)
    var bulletAngle = 0f
    var bulletSpeed = 0f
    var canShoot = true
    var aimTouched = false

    override fun create() {
        camera = OrthographicCamera()
        viewport = ExtendViewport(vp_width, vp_height, camera)
        shapes = ShapeRenderer()
        batch = SpriteBatch()

        val touchpadStyle = Touchpad.TouchpadStyle()
        touchpadStyle.background = TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("textures/touchBackground.png"))))
        touchpadStyle.knob = TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("textures/touchKnob.png"))))

        moveTouchPad = Touchpad(10f, touchpadStyle)
        moveTouchPad.setBounds(15f, 15f, 250f, 250f)

        aimTouchPad = Touchpad(10f, touchpadStyle)
        aimTouchPad.setBounds(750f, 15f, 250f, 250f)

        stage = Stage()
        stage.addActor(moveTouchPad)
        stage.addActor(aimTouchPad)
        Gdx.input.inputProcessor = stage
    }

    override fun render() {
        control(Gdx.graphics.deltaTime)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.projectionMatrix = camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color.RED
        shapes.circle(playerPos.x, playerPos.y, 1f)
        shapes.color = Color.GREEN
        shapes.circle(targetPos.x, targetPos.y, 2f)
        if (aimTouchPad.isTouched and canShoot) {
            shapes.color = Color.BLUE
            shapes.line(playerPos, aimVector)
        }
        shapes.color = Color.YELLOW
        shapes.circle(bulletPos.x, bulletPos.y, 1f)
        shapes.end()

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    fun control(delta: Float) {
        playerPos.add(moveTouchPad.knobPercentX * speed, moveTouchPad.knobPercentY * speed, 0f)

        targetPos.rotateAroundPoint(angularSpeed, targetCenterPos)

        if (aimTouchPad.isTouched) {
            aimVector = playerPos.cpy().add(Vector3(aimTouchPad.knobPercentX, aimTouchPad.knobPercentY, 0f).nor().scl(aimMultiplier))
        }

        if (aimTouchPad.isTouched and !moveTouchPad.isTouched) {
            aimTouched = true
            aimMultiplier = Math.min(aimMultiplier + aimMultiplierSpeed * delta, maxAimMultiplier)
        } else {
            aimMultiplier = minAimMultiplier
            if (!aimTouchPad.isTouched and canShoot and aimTouched) {
                bulletPos.set(playerPos)
                bulletAngle = playerPos.angleProjectedXY(aimVector)
                bulletSpeed = aimMultiplier
                canShoot = false
                val executor = Intervals()
                executor.deffer(2, TimeUnit.SECONDS, {
                    canShoot = true
                })
            }
            aimTouched = false
        }

        bulletPos.sub(Vector3(bulletSpeed, 0f, 0f).rotate(bulletAngle, 0f, 0f, 1f))
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        stage.viewport.update(width, height, true);
    }

    override fun dispose() {
        shapes.dispose()
        stage.dispose()
    }

    fun Vector3.rotateAroundPoint(angle: Float, pointPos: Vector3) {
        this.sub(pointPos)
        this.rotate(angle, 0f, 0f, 1f)
        this.add(pointPos)
    }

    fun Vector3.angleProjectedXY(target: Vector3): Float {
        return (Vector2(this.x, this.y).sub(Vector2(target.x, target.y))).angle()
    }
}
