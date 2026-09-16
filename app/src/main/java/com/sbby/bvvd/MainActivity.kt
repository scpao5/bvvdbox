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

class MainActivity : Activity() {

    private lateinit var sfx: Sfx
    private lateinit var music: MusicPlayer

    private val musicButtons = LinkedHashMap<String, TextView>()

    private class Item(val label: String, val file: String, val long: Boolean)
    private class Group(val title: String, val items: List<Item>)

    private val groups: List<Group> = listOf(
        Group(
            "战术语音",
            listOf(
                Item("进攻A点", "进攻A点.mp3", false),
                Item("进攻B点", "进攻B点.mp3", false),
                Item("进攻C点", "进攻C点.mp3", false),
                Item("进攻D点", "进攻D点.mp3", false),
                Item("攻击基地", "攻击基地.mp3", false),
                Item("攻击部队", "攻击部队.mp3", false),
                Item("防守A点", "防守A点.mp3", false),
                Item("防守B点", "防守B点.mp3", false),
                Item("防守C点", "防守C点.mp3", false),
                Item("防守D点", "防守D点.mp3", false),
                Item("注意标识区域", "注意标识区域.mp3", false),
                Item("准备着陆", "准备着陆.mp3", false),
                Item("收到", "收到.mp3", false),
                Item("不行", "不行.mp3", false),
                Item("抱歉", "抱歉.mp3", false),
                Item("感谢", "感谢.mp3", false),
                Item("正在修理", "正在修理.mp3", false),
                Item("正在返回基地", "正在返回基地.mp3", false),
                Item("跟我来", "跟我来.mp3", false),
                Item("掩护我", "掩护我.mp3", false),
                Item("干得好", "干得好.mp3", false),
                Item("正在装填", "正在装填.mp3", false),
                Item("巴巴打捏", "巴巴打捏.mp3", false),
            )
        ),
        Group(
            "游戏音效",
            listOf(
                Item("敌方夺取战区", "敌方夺取战区.mp3", false),
                Item("我方夺取战区", "我方夺取战区.mp3", false),
                Item("局势不太妙", "局势不太妙.mp3", false),
                Item("局势很有利", "局势很有利.mp3", false),
                Item("即将胜利", "即将胜利.mp3", false),
                Item("即将失败", "即将失败.mp3", false),
                Item("受击音效1", "受击音效1.mp3", false),
                Item("受击音效2", "受击音效2.mp3", false),
                Item("目标摧毁", "目标摧毁.mp3", false),
                Item("目标消失", "目标消失.mp3", false),
                Item("目标已消灭", "目标已消灭.mp3", false),
                Item("命中", "命中.mp3", false),
                Item("开饭了", "开饭了.mp3", false),
                Item("击毁坦克", "击毁坦克.mp3", false),
                Item("击毁飞机", "击毁飞机.mp3", false),
                Item("网络状态不佳", "网络状态不佳.mp3", false),
                Item("按计划打扫战场", "按计划打扫战场.mp3", false),
                Item("胜败乃兵家常事1", "胜败乃兵家常事1.mp3", false),
                Item("胜败乃兵家常事2", "胜败乃兵家常事2.mp3", false),
                Item("胜败乃兵家常事3", "胜败乃兵家常事3.mp3", false),
                Item("进入攻击起始点", "进入攻击起始点.mp3", false),
                Item("进入预定航线", "进入预定航线.mp3", false),
                Item("前往目标地点", "前往目标地点.mp3", false),
                Item("游戏开始", "游戏开始.mp3", false),
                Item("获得金币", "获得金币.mp3", false),
                Item("花钱消费", "花钱消费.mp3", false),
                Item("领取任务", "领取任务.mp3", false),
                Item("获得奖励", "获得奖励.mp3", false),
                Item("抽奖开始", "抽奖开始.mp3", false),
                Item("抽奖_转动循环", "抽奖_转动循环.mp3", false),
                Item("抽奖_停止", "抽奖_停止.mp3", false),
                Item("抽奖_格子升起", "抽奖_格子升起.mp3", false),
                Item("切换载具", "切换载具.mp3", false),
            )
        ),
        Group(
            "战歌 / 音乐",
            listOf(
                Item("复仇天使", "复仇天使.mp3", true),
                Item("伟大的祖国", "伟大的祖国.mp3", true),
                Item("红星复仇者", "红星复仇者.mp3", true),
                Item("前进澳大利亚", "前进澳大利亚.mp3", true),
                Item("保卫海岸", "保卫海岸.flac", true),
                Item("斯拉夫雷霆", "斯拉夫雷霆.mp3", true),
                Item("丹妮丝之歌", "丹妮丝之歌.flac", true),
                Item("光荣飞行", "光荣飞行.mp3", true),
                Item("念巴兰尼科夫", "念巴兰尼科夫.mp3", true)
            )
        )
    )

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
