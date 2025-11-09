package dk.holonet.utils

import java.awt.Point
import java.awt.Cursor
import java.awt.Toolkit
import java.awt.image.BufferedImage

internal fun createEmptyCursor(): Cursor {
    return Toolkit.getDefaultToolkit().createCustomCursor(
        BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB),
        Point(0, 0),
        "Empty Cursor"
    )
}