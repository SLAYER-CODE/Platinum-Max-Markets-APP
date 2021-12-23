package com.example.finalproductos.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


class AnimationsUtils {
    fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim: Animation = ScaleAnimation(
            1f, 1f,  // Start and end values for the X axis scaling
            startScale, endScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 1000
        v.startAnimation(anim)
    }

    fun getSpringAnimation(
        view: View,
        springAnimationType: FloatPropertyCompat<View>,
        finalPosition: Float
    ): SpringAnimation {
        val animation = SpringAnimation(view, springAnimationType)
        // create a spring with desired parameters
        val spring = SpringForce()
        spring.finalPosition = finalPosition
        spring.stiffness = SpringForce.STIFFNESS_VERY_LOW // optional
        spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY // optional
        // set your animation's spring
        animation.spring = spring
        return animation
    }
}