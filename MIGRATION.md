# RemixIcon-Kmp v0.0.4 迁移指南

## 变更概述

从 v0.0.2 → v0.0.4，图标加载方式发生了**根本性变化**：

| 项目 | v0.0.2（旧） | v0.0.4（新） |
|------|-------------|-------------|
| 图标加载方式 | `ImageVector` Kotlin 类（编译进 DEX） | CMP Resources 资源文件（运行时按需加载） |
| 图标属性类型 | `ImageVector` | `DrawableResource`（非 Composable 可直接传递） |
| 属性命名 | PascalCase: `CameraFill` | **camelCase**: `cameraFill` |
| 渲染方式 | `Icon(imageVector = RemixIcon.xxx)` | `painterResource()` / `vectorResource()` 转换 |
| 包体积 | 全部图标编译进 DEX | XML 资源文件，按需加载 |
| 编译速度 | 慢（3000+ 类） | 快（仅 1 个包装类） |

## API 变更

### 旧用法（v0.0.2）

```kotlin
import com.lalilu.RemixIcon

// 非 Composable 环境：直接传 ImageVector
val icon: ImageVector = RemixIcon.Media.CameraFill

// Composable 环境：直接使用（ImageVector 是 Icon 的默认参数）
Icon(
    imageVector = RemixIcon.Media.CameraFill,
    contentDescription = "camera"
)
```

### 新用法（v0.0.4）

```kotlin
import com.lalilu.RemixIcon
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.resources.painterResource

// 非 Composable 环境：传 DrawableResource 引用（类型变了，命名 camelCase）
val icon: DrawableResource = RemixIcon.Media.cameraFill

// Composable 环境：需要调用 vectorResource() 或 painterResource()
Icon(
    painter = painterResource(RemixIcon.Media.cameraFill),
    contentDescription = "camera"
)
// 或
Icon(
    imageVector = vectorResource(RemixIcon.Media.cameraFill),
    contentDescription = "camera"
)

// 遍历所有图标
val allIcons = RemixIcon.AllIcons
```

### 命名对照表

| SVG 文件名 | v0.0.2（PascalCase） | v0.0.4（camelCase） |
|-----------|---------------------|-------------------|
| `ancient-gate-fill.svg` | `Buildings.AncientGateFill` | `Buildings.ancientGateFill` |
| `camera-fill.svg` | `Media.CameraFill` | `Media.cameraFill` |
| `arrow-down-fill.svg` | `Arrows.ArrowDownFill` | `Arrows.arrowDownFill` |
| `a-b.svg` | `Editor.AB` | `Editor.aB` |

## 迁移步骤

### 1. 更新依赖版本

```kotlin
implementation("io.github.cy745:remixicon-kmp:0.0.4")
```

### 2. 替换所有 `ImageVector` 类型为 `DrawableResource`

```kotlin
// 旧
val icon: ImageVector = RemixIcon.Media.CameraFill
// 新
val icon: DrawableResource = RemixIcon.Media.cameraFill
```

### 3. 更新图标属性名为 camelCase

所有图标属性名从 PascalCase 改为 camelCase。批量替换可以用正则：

```
搜索: RemixIcon\.(\w+)\.([A-Z]\w+)
替换: RemixIcon.$1.${2:decapitalize}
```

### 4. 为所有 `Icon()` 调用添加 `painterResource()`

```kotlin
// 旧
Icon(imageVector = RemixIcon.Media.cameraFill, ...)
// 新
Icon(painter = painterResource(RemixIcon.Media.cameraFill), ...)
```

### 5. 推荐：全局辅助函数（可选）

```kotlin
@Composable
fun RemixIconIcon(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) = Icon(
    painter = painterResource(icon),
    contentDescription = null,
    modifier = modifier,
    tint = tint,
)
```

```kotlin
// 使用
RemixIconIcon(RemixIcon.Media.cameraFill, modifier = Modifier.size(24.dp))
```

### 6. 着色

图标默认颜色为 `#000000`，请通过 `tint` 参数着色：

```kotlin
Icon(
    painter = painterResource(RemixIcon.Arrows.arrowDownFill),
    contentDescription = null,
    tint = MaterialTheme.colorScheme.primary
)
```

## 验证清单

- [ ] 依赖版本已更新到 `0.0.4`
- [ ] 所有 `ImageVector` 类型声明已替换为 `DrawableResource`
- [ ] 所有图标属性名已改为 camelCase
- [ ] 所有 Composable 渲染处已添加 `painterResource()` 或 `vectorResource()`
- [ ] 项目能正常编译通过
- [ ] 图标在运行时显示正常
