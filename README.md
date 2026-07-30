# RemixIcon-Kmp

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cy745/remixicon-kmp)](https://central.sonatype.com/artifact/io.github.cy745/remixicon-kmp)
![CI](https://github.com/cy745/RemixIcon-Kmp/actions/workflows/ci.yml/badge.svg)

A Kotlin Multiplatform library that wraps [Remix Icon](https://remixicon.com) as Compose Multiplatform Resources. Provides type-safe `DrawableResource` accessors for all **3229+ icons** across **20 categories**.

## 安装

```kotlin
// commonMain dependencies
implementation("io.github.cy745:remixicon-kmp:0.0.4")
```

## 使用

```kotlin
import com.lalilu.RemixIcon
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.resources.painterResource

// 非 Composable 环境：传递 DrawableResource 引用
val iconRef = RemixIcon.Media.cameraFill

// Composable 环境：渲染
@Composable
fun MyIcon() {
    Icon(
        painter = painterResource(RemixIcon.Media.cameraFill),
        contentDescription = "camera"
    )
    // 或使用 ImageVector
    Icon(
        imageVector = vectorResource(RemixIcon.Media.cameraFill),
        contentDescription = "camera"
    )
}

// 遍历所有图标
val allIcons = RemixIcon.AllIcons
```

### 着色

由于图标默认颜色为 `#000000`，渲染时通过 `tint` 着色：

```kotlin
Icon(
    painter = painterResource(RemixIcon.Arrows.arrowDownFill),
    contentDescription = null,
    tint = Color.Red  // 或 theme color
)
```

### 查看图标列表

所有分类和图标名可参考 [完整图标索引](https://remixicon.com)。

## 目录结构

| 分类 | 图标数 |
|------|--------|
| Arrows | 178 |
| Buildings | 62 |
| Business | 220 |
| Communication | 92 |
| Design | 236 |
| Development | 66 |
| Device | 192 |
| Document | 244 |
| Editor | 151 |
| Finance | 172 |
| Food | 34 |
| GameAndSports | 50 |
| Health & Medical | 84 |
| Logos | 300 |
| Map | 172 |
| Media | 296 |
| Others | 116 |
| System | 348 |
| User & Faces | 134 |
| Weather | 82 |

## 从 v0.0.2 迁移

请参阅 [MIGRATION.md](MIGRATION.md)。

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
