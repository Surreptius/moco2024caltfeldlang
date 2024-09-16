package edu.moco.pomtodoro.utils

import java.util.Locale

fun formatTimer(currentSeconds: Long): String =
    "${formatMinutes(currentSeconds)}:${formatSeconds(currentSeconds)}"

fun formatMinutes(currentSeconds: Long): String {
    val minutes = currentSeconds / 60

    return String.format(Locale.US, "%02d", minutes)
}

fun formatSeconds(currentSeconds: Long): String {
    val seconds = currentSeconds % 60

    return String.format(Locale.US, "%02d", seconds)
}
