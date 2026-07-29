# RemixIcon-Kmp

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cy745/remixicon-kmp)](https://central.sonatype.com/artifact/io.github.cy745/remixicon-kmp)
![CI](https://github.com/cy745/RemixIcon-Kmp/actions/workflows/ci.yml/badge.svg)

A Kotlin Multiplatform library that wraps [Remix Icon](https://remixicon.com) as Compose Multiplatform Resources. Provides type-safe `DrawableResource` accessors for all 3229+ icons across 19 categories.

## 安装

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
}

// commonMain dependencies
implementation("io.github.cy745:remixicon-kmp:0.0.3")
```

## 使用

```kotlin
import com.lalilu.RemixIcon
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.resources.painterResource

// 非 Composable 环境：传递 DrawableResource 引用
val iconRef = RemixIcon.Media.CameraFill

// Composable 环境：渲染
@Composable
fun MyIcon() {
    Icon(
        painter = painterResource(RemixIcon.Media.CameraFill),
        contentDescription = "camera"
    )
    // 或使用 ImageVector
    Icon(
        imageVector = vectorResource(RemixIcon.Media.CameraFill),
        contentDescription = "camera"
    )
}

// 遍历所有图标
val allIcons = RemixIcon.AllIcons
```

## 目录结构

| 分类 | 图标数 |
|------|--------|
| Arrows | 336 |
| Buildings | 44 |
| Business | 380 |
| Communication | 186 |
| Design | 98 |
| Development | 138 |
| Device | 210 |
| Document | 106 |
| Editor | 228 |
| Finance | 80 |
| Food | 102 |
| Health & Medical | 116 |
| Logos | 172 |
| Map | 192 |
| Media | 262 |
| Others | 28 |
| System | 342 |
| User & Faces | 210 |
| Weather | 82 |

## 图标许可

本库中的图标来自 [Remix Icon](https://remixicon.com)，基于 [Remix Icon License v1.0](LICENSE-REMIXICON.md) 许可使用。

Remix Icon License v1.0 允许：
- ✅ 商业及非商业使用
- ✅ 修改和自定义（颜色、大小等）
- ✅ 在软件产品、网站、移动应用中使用
- ❌ 禁止作为独立图标包销售
- ❌ 禁止用于 Logo 或商标

详情请参阅 [LICENSE-REMIXICON.md](LICENSE-REMIXICON.md)。

## 项目许可

本库的代码（生成器、封装代码等）基于 Apache 2.0 许可，详见 [LICENSE](LICENSE)。
