package com.pavel.hrolovic

data class Slice(val pointX: Float,
                 val pointY: Float,
                 val width: Float,
                 val height: Float) {
    override fun toString(): String = "pointX = $pointX, pointY = $pointY, width = $width, height = $height, area = ${width * height}"

}