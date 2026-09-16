package com.sbby.bvvd

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class Sfx(context: Context) {

    private val pool: SoundPool
    private val ids = HashMap<String, Int>()

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        pool = SoundPool.Builder()
            .setMaxStreams(16)
            .setAudioAttributes(attrs)
            .build()
        val appCtx = context.applicationContext
        val names = try {
            appCtx.assets.list("audio")
        } catch (e: Exception) {
            null
        }
        if (names != null) {
            for (n in names) {
                try {
                    val afd = appCtx.assets.openFd("audio/" + n)
                    val id = pool.load(afd, 1)
                    afd.close()
                    ids[n] = id
                } catch (e: Exception) {
                }
            }
        }
    }

    fun play(fileName: String) {
        val id = ids[fileName] ?: return
        pool.play(id, 1f, 1f, 1, 0, 1f)
    }

    fun stopAll() {
        pool.autoPause()
    }
}
