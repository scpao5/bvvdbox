# bvvd盒 项目状态（交接文档）

> 生成于 2026-09-22，用于新会话快速接续

## 一、项目基本信息
- 路径: `/storage/emulated/0/AppProjects/BvvdBox`
- 包名: `com.sbby.bvvd`   应用名: bvvd盒
- 版本: versionCode 4 / versionName 1.1
- minSdk 29 / targetSdk 34 / compileSdk 37 / AGP 9.2.1 / Gradle 9.5.1
- 签名: 仅 **V2**（KEYSTORE 属性名不变，配置在 `~/.gradle/gradle.properties`，**禁止读取内容**）
- 3 个 Kotlin 文件: `MainActivity.kt` / `Sfx.kt` / `MusicPlayer.kt`
- 成品: `/storage/emulated/0/AppProjects/bvvd盒_v1.1.apk` (112.5MB)
- GitHub: `scpao5/bvvdbox`（已发布 Release v1.1）

## 二、当前架构（2026-09-22 重构）
**UI 数据驱动**：`assets/config.json` 负责排序/映射，代码只负责渲染
```
assets/
├─ config.json     ← 唯一数据源
├─ audio/          57 个音效 mp3/ogg
└─ music/          12 首音乐 mp3/flac
```
config.json 结构:
```json
{ "version":1,
  "groups":[
    { "title":"战术语音", "items":[ {"label":"进攻A点","file":"audio/进攻A点.mp3","type":"sfx"} ] },
    { "title":"战歌 / 音乐", "items":[ {"label":"安东星的家","file":"music/安东星的家.mp3","type":"music"} ] }
  ] }
```
- `type`: `sfx`(SoundPool短音效) / `music`(MediaPlayer整首)
- **加音频流程**: ①mp3丢进 audio/ 或 music/ → ②config.json 加一行 → ③`./gradlew assembleRelease`（7~10秒，不碰Kotlin）

## 三、按钮清单（69 个，3 组）
- 战术语音 24 / 游戏音效 33 / 战歌·音乐 12
- 战歌 12 首: 复仇天使、伟大的祖国、红星复仇者、前进澳大利亚、保卫海岸、斯拉夫雷霆、丹妮丝之歌、光荣飞行、念巴兰尼科夫、自由钟声、红星复仇者恶搞版、安东星的家

## 四、UI 风格（对齐 aqzlgj）
- 背景 `#F0F4F8`、顶栏 `#2196F3`+白粗体字、分隔线 `#1976D2`
- 卡片白 `#FFFFFF`、按下 `#E3F2FD`、文字 `#424242`、圆角 14dp
- 停止全部 `#F44336`、3列网格(每格高52dp/间距4dp)
- **`row!!.isBaselineAligned = false`** ← 关键修复(防长文本按钮下沉)

## 五、最近修复记录
- ✅ config.json 数据驱动重构（MainActivity 308→259行）
- ✅ 补回漏掉的『安东星的家』
- ✅ 剔除 3 个未引用音频(胜利音效/获得载具/获得银狮)
- ✅ 修复 isBaselineAligned（长文本按钮下沉）
- git 最新: `5d6ef70`

## 六、待办
1. ~~获得载具音效~~（已放弃，找不到）
2. ~~胜利音效后半~~（已放弃，疑似运行时拼接）
3. 按钮名过长问题（"红星复仇者恶搞版"9字，可缩为"复仇者恶搞版"）
4. 混淆版稳定性真机验证（已安装待测）
5. 需要时可推 GitHub（`git push --force`，因历史不兼容）

## 七、环境要点（Termux）
- 编译环境在 `~/.bashrc`（JAVA_HOME/SDK/TMPDIR），**不要手动 export**
- aapt2 需 `-Pandroid.aapt2FromMavenOverride=$PREFIX/bin/aapt2`（已写入 gradle.properties）
- 编译: `bash -c 'source ~/.bashrc; cd 项目; bash ./gradlew assembleRelease'`
- GitHub 走 gh-proxy；SSH key: `~/.ssh/id_ed25519_gitee`
- 改 sdcard 文件必须用 python(open/write)，`sed -i` 会静默失败
