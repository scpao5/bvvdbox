package com.sbby.bvvd

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 音效池(低延迟优化版)
 *  - 后台线程异步加载, 不阻塞主线程
 *  - 小文件优先加载(常用短音效先就绪)
 *  - 首次加载完成时静默预热, 提前打开音频通道
 */
class Sfx(context: Context) {

    private val pool: SoundPool
    private val ids = HashMap<String, Int>()
    private val loadedSet = HashSet<Int>()
    private val preheated = AtomicBoolean(false)
    private val exec = Executors.newSingleThreadExecutor()

    @Volatile
    var onReady: (() -> Unit)? = null

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        pool = SoundPool.Builder()
            .setMaxStreams(16)
            .setAudioAttributes(attrs)
            .build()

        // 加载完成回调: 首次完成时静默预热一次, 打开音频通道
        pool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status != 0) return@setOnLoadCompleteListener
            synchronized(loadedSet) { loadedSet.add(sampleId) }
            if (preheated.compareAndSet(false, true)) {
                pool.play(sampleId, 0f, 0f, 1, 0, 1f)
            }
        }

        val appCtx = context.applicationContext
        exec.execute {
            val names = try {
                appCtx.assets.list("audio")
            } catch (e: Exception) {
                null
            } ?: return@execute

            // 小文件优先
            val sorted = names.sortedBy { n ->
                try { appCtx.assets.openFd("audio/$n").use { it.length } } catch (e: Exception) { Long.MAX_VALUE }
            }
            for (n in sorted) {
                try {
                    val afd = appCtx.assets.openFd("audio/" + n)
                    val id = pool.load(afd, 1)
                    afd.close()
                    synchronized(ids) { ids[n] = id }
                } catch (e: Exception) {
                }
            }
            onReady?.invoke()
        }
    }

    fun isLoaded(fileName: String): Boolean {
        val id = synchronized(ids) { ids[fileName] } ?: return false
        synchronized(loadedSet) { return loadedSet.contains(id) }
    }

    fun play(fileName: String) {
        val id = synchronized(ids) { ids[fileName] } ?: return
        pool.play(id, 1f, 1f, 1, 0, 1f)
    }

    fun stopAll() {
        pool.autoPause()
    }

    fun release() {
        try { pool.release() } catch (e: Exception) {}
        exec.shutdown()
    }
}
