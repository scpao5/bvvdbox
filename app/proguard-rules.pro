# bvvd盒 —— 最小保留规则
# Manifest 引用的组件本身会被 R8 自动保留，这里只加保险
-keep class com.sbby.bvvd.MainActivity { *; }
-dontwarn **
