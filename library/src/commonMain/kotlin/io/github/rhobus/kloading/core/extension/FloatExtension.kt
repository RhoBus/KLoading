package io.github.rhobus.kloading.core.extension

import kotlin.math.PI

/**
 * Converts degrees (Float) to radians (Double).
 * Formula: r = θ * (π / 180)
 * or radians = degrees * (PI / 180)
 */
val Float.toRadians : Double get() = this * (PI / 180.0)
