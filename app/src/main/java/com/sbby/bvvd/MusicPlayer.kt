package com.sbby.bvvd

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer

class MusicPlayer(private val context: Context) {

    private var player: MediaPlayer? = null
    private var currentFile: String? = null

    var onChanged: (() -> Unit)? = null

    fun isPlaying(file: String): Boolean = currentFile == file && player != null

    fun toggle(file: String) {
        if (currentFile == file && player != null) {
            stop()
            return
        }
        stop()
        val p = MediaPlayer()
        p.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        try {
            val afd = context.assets.openFd("music/" + file)
            p.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            p.setOnCompletionListener { stop() }
            p.prepare()
            p.start()
            player = p
            currentFile = file
        } catch (e: Exception) {
            try {
                p.release()
            } catch (e2: Exception) {
            }
            player = null
            currentFile = null
        }
        onChanged?.invoke()
    }

    fun stop() {
        val p = player
        player = null
        currentFile = null
        if (p != null) {
            try {
                p.stop()
            } catch (e: Exception) {
            }
            try {
                p.release()
            } catch (e: Exception) {
            }
        }
        onChanged?.invoke()
    }
}
