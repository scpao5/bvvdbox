# bvvd盒

《战争雷霆》语音盒 —— 原生 Kotlin 重写版

> 播放游戏中解包出的音效 / 语音 / 战歌，随时整活 🎵

---

## 📥 下载

**最新版：v1.1**

| 来源 | 链接 |
|---|---|
| **GitHub（推荐, 带代理）** | [bvvdbox_1.1.apk](https://gh-proxy.com/https://github.com/scpao5/bvvdbox/releases/download/v1.1/bvvdbox_1.1.apk) |
| GitHub 原链 | [bvvdbox_1.1.apk](https://github.com/scpao5/bvvdbox/releases/download/v1.1/bvvdbox_1.1.apk) |
| 全部版本 | [Releases](https://gh-proxy.com/https://github.com/scpao5/bvvdbox/releases) |

- 包名：`com.sbby.bvvd`
- 体积：约 108 MB（音频占绝大部分）
- 签名：仅 V2
- 权限：**不需要任何权限、不联网**

---

## ✨ 功能

### 分组
- **战术语音**（24 个）：进攻/防守 A~D 点、掩护我、干得好、巴巴达涅、这路你是多熟呢 …
- **游戏音效**（33 个）：击毁坦克/飞机、目标摧毁、开饭了、抽奖系列、切换载具 …
- **战歌 / 音乐**（12 首）：复仇天使、光荣飞行、念巴兰尼科夫、自由钟声、安东星的家 …

### 播放
- 短音效：`SoundPool`（16 流，可叠加）
- 音乐：`MediaPlayer`（支持 mp3 / flac，切后台/锁屏继续）
- 点同一首再点停止，点别的自动切歌

---

## 🛠 技术

- 纯原生 Kotlin + Android View（无 Compose / 无第三方 UI 库）
- minSdk 29 / targetSdk 34，AGP 9.2.1
- 音频资源放 `assets/audio/`（音效）和 `assets/music/`（音乐）

---

## 🎯 音效来源

从《战争雷霆手游 1.26.0》解包（FMOD bank → Vorbis/FADPCM）。
详见整理记录：`yyds/_战雷整理/README.md`

---

## 📜 许可

GPL-3.0
