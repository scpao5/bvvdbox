package com.sbby.bvvd

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONObject

class MainActivity : Activity() {

    private lateinit var sfx: Sfx
    private lateinit var music: MusicPlayer

    private val musicButtons = LinkedHashMap<String, TextView>()

    private class Item(val label: String, val file: String, val long: Boolean)
    private class Group(val title: String, val items: List<Item>)

    // ---- 数据来源: assets/config.json (数据驱动) ----
    private fun loadGroups(): List<Group> {
        val groups = ArrayList<Group>()
        val json = try {
            assets.open("config.json").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            null
        }
        if (json == null) return groups
        try {
            val root = JSONObject(json)
            val arr = root.getJSONArray("groups")
            for (i in 0 until arr.length()) {
                val g = arr.getJSONObject(i)
                val title = g.optString("title", "")
                val items = ArrayList<Item>()
                val itArr = g.optJSONArray("items") ?: continue
                for (j in 0 until itArr.length()) {
                    val o = itArr.getJSONObject(j)
                    val label = o.optString("label", "")
                    val file = o.optString("file", "")
                    val type = o.optString("type", "sfx")
                    if (file.isEmpty()) continue
                    // file 形如 "audio/xxx.mp3" 或 "music/xxx.mp3"
                    val isMusic = (type == "music") || file.startsWith("music/")
                    val name = file.substringAfterLast('/')
                    items.add(Item(label, name, isMusic))
                }
                groups.add(Group(title, items))
            }
        } catch (e: Exception) {
            // json 解析失败 → 返回空, UI 会显示"无内容"
        }
        return groups
    }

    // ---- Material 浅色配色（对齐 aqzlgj）----
    private val C_BG = Color.parseColor("#F0F4F8")
    private val C_PRIMARY = Color.parseColor("#2196F3")
    private val C_PRIMARY_DARK = Color.parseColor("#1976D2")
    private val C_PRIMARY_LIGHT = Color.parseColor("#BBDEFB")
    private val C_PRESS = Color.parseColor("#E3F2FD")
    private val C_CARD = Color.WHITE
    private val C_CARD_EDGE = Color.parseColor("#E5E7EB")
    private val C_TEXT = Color.parseColor("#424242")
    private val C_STOP = Color.parseColor("#F44336")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sfx = Sfx(this)
        music = MusicPlayer(this)
        buildUi()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        music.onChanged = { runOnUiThread { refreshMusic() } }
    }

    private fun buildUi() {
        val groups = loadGroups()
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(C_BG)

        // ---- 顶栏：纯蓝 + 白粗字 ----
        val top = LinearLayout(this)
        top.orientation = LinearLayout.HORIZONTAL
        top.gravity = Gravity.CENTER_VERTICAL
        top.setBackgroundColor(C_PRIMARY)
        top.setPadding(dp(20), dp(14), dp(20), dp(14))
        root.addView(top, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        val title = TextView(this)
        title.text = "bvvd盒"
        title.setTextColor(Color.WHITE)
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        title.setTypeface(Typeface.DEFAULT_BOLD)
        top.addView(title, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

        val sub = TextView(this)
        sub.text = "War Thunder 语音盒"
        sub.setTextColor(C_PRIMARY_LIGHT)
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        top.addView(sub)

        val divider = View(this)
        divider.setBackgroundColor(C_PRIMARY_DARK)
        root.addView(divider, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(2)))

        // ---- 内容区 ----
        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.setPadding(dp(12), dp(6), dp(12), dp(18))
        scroll.addView(content, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        root.addView(scroll, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))

        for (g in groups) {
            val head = LinearLayout(this)
            head.orientation = LinearLayout.HORIZONTAL
            head.gravity = Gravity.CENTER_VERTICAL
            val hp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            hp.topMargin = dp(16)
            hp.bottomMargin = dp(8)
            hp.leftMargin = dp(4)
            content.addView(head, hp)

            val bar = View(this)
            bar.setBackground(round(C_PRIMARY, dp(2)))
            head.addView(bar, LinearLayout.LayoutParams(dp(4), dp(18)))

            val ht = TextView(this)
            ht.text = g.title
            ht.setTextColor(C_TEXT)
            ht.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            ht.setTypeface(Typeface.DEFAULT_BOLD)
            val htp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            htp.leftMargin = dp(8)
            head.addView(ht, htp)

            var row: LinearLayout? = null
            g.items.forEachIndexed { i, item ->
                if (i % 3 == 0) {
                    row = LinearLayout(this)
                    row!!.orientation = LinearLayout.HORIZONTAL
                    row!!.baselineAligned = false
                    content.addView(row, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                }
                row!!.addView(makeButton(item))
            }
            val rem = g.items.size % 3
            if (rem != 0) {
                for (k in rem until 3) {
                    val sp = View(this)
                    val lp = LinearLayout.LayoutParams(0, dp(52), 1f)
                    lp.marginStart = dp(4)
                    lp.marginEnd = dp(4)
                    lp.bottomMargin = dp(8)
                    row!!.addView(sp, lp)
                }
            }
        }

        // ---- 底部“停止全部” ----
        val stop = TextView(this)
        stop.text = "\u25A0  停止全部"
        stop.gravity = Gravity.CENTER
        stop.setTextColor(Color.WHITE)
        stop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        stop.setBackgroundColor(C_STOP)
        stop.isClickable = true
        stop.setOnClickListener {
            sfx.stopAll()
            music.stop()
        }
        root.addView(stop, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(52)))

        setContentView(root)
    }

    private fun makeButton(item: Item): TextView {
        val tv = TextView(this)
        tv.text = if (item.long) "\u266A " + item.label else item.label
        tv.gravity = Gravity.CENTER
        tv.setTextColor(C_TEXT)
        tv.maxLines = 1
        tv.setAutoSizeTextTypeUniformWithConfiguration(10, 14, 1, TypedValue.COMPLEX_UNIT_SP)
        tv.setBackground(buttonBg())
        tv.isClickable = true
        tv.isFocusable = true
        val lp = LinearLayout.LayoutParams(0, dp(52), 1f)
        lp.marginStart = dp(4)
        lp.marginEnd = dp(4)
        lp.bottomMargin = dp(8)
        tv.layoutParams = lp
        if (item.long) musicButtons[item.file] = tv
        tv.setOnClickListener {
            if (item.long) {
                sfx.stopAll()
                music.toggle(item.file)
            } else {
                sfx.play(item.file)
            }
        }
        return tv
    }

    private fun refreshMusic() {
        for ((file, tv) in musicButtons) {
            if (music.isPlaying(file)) {
                tv.background = round(C_PRIMARY, dp(14))
                tv.setTextColor(Color.WHITE)
            } else {
                tv.background = buttonBg()
                tv.setTextColor(C_TEXT)
            }
        }
    }

    private fun buttonBg(): StateListDrawable {
        val s = StateListDrawable()
        s.addState(intArrayOf(android.R.attr.state_pressed), round(C_PRESS, dp(14)))
        s.addState(intArrayOf(), roundEdge(C_CARD, dp(14), dp(1), C_CARD_EDGE))
        return s
    }

    private fun round(color: Int, radius: Int): GradientDrawable {
        val g = GradientDrawable()
        g.shape = GradientDrawable.RECTANGLE
        g.setColor(color)
        g.cornerRadius = radius.toFloat()
        return g
    }

    private fun roundEdge(color: Int, radius: Int, stroke: Int, strokeColor: Int): GradientDrawable {
        val g = round(color, radius)
        g.setStroke(stroke, strokeColor)
        return g
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    override fun onPause() {
        super.onPause()
        if (::sfx.isInitialized) sfx.stopAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::music.isInitialized) music.stop()
    }
}
