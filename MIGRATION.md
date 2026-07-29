# RemixIcon-Kmp v0.0.3 迁移指南

## 变更概述

RemixIcon-Kmp 从 v0.0.2 升级到 v0.0.3 时，图标加载方式发生了**根本性变化**：

| 项目 | v0.0.2（旧） | v0.0.3（新） |
|------|-------------|-------------|
| 图标加载方式 | SVG → `ImageVector` Kotlin 类（编译进 DEX） | SVG → CMP Resources（运行时按需加载） |
| 图标属性类型 | `ImageVector` | `DrawableResource` |
| 渲染方式 | 直接使用 `Icon()` | 需要 `painterResource()` 或 `vectorResource()` |
| 包体积 | 全部图标编译进 DEX | XML 资源文件，按需加载 |
| 编译速度 | 慢（3058+ 个类） | 快（仅 1 个包装类） |

## API 变更

### 旧用法（v0.0.2）

```kotlin
import com.lalilu.RemixIcon

// 非 Composable 环境：直接传 ImageVector
val icon: ImageVector = RemixIcon.Media.CameraFill

// Composable 环境：直接使用
Icon(
    imageVector = RemixIcon.Media.CameraFill,
    contentDescription = "camera"
)
```

### 新用法（v0.0.3）

```kotlin
import com.lalilu.RemixIcon
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.resources.painterResource

// 非 Composable 环境：传 DrawableResource 引用（类型变了）
val icon: DrawableResource = RemixIcon.Media.CameraFill

// Composable 环境：需要调用 vectorResource() 或 painterResource()
// 方式一：用 Painter
Icon(
    painter = painterResource(RemixIcon.Media.CameraFill),
    contentDescription = "camera"
)

// 方式二：用 ImageVector（推荐）
Icon(
    imageVector = vectorResource(RemixIcon.Media.CameraFill),
    contentDescription = "camera"
)
```

## 迁移步骤

### 1. 更新依赖版本

```kotlin
// build.gradle.kts
implementation("io.github.cy745:remixicon-kmp:0.0.3")
```

### 2. 替换所有 `ImageVector` 类型引用

```
搜索: val (\w+): ImageVector = RemixIcon.
替换: val $1: DrawableResource = RemixIcon.
```

或手动调整——所有声明为 `ImageVector` 类型的变量、参数、属性都需要改为 `DrawableResource`。

### 3. 为所有 `Icon()` 调用添加包装函数

**搜索模式：**
```kotlin
Icon(
    imageVector = RemixIcon.XXX,
    ...
)
```

**改为：**
```kotlin
Icon(
    painter = painterResource(RemixIcon.XXX),
    ...
)
```

或保持 `imageVector` 参数：
```kotlin
Icon(
    imageVector = vectorResource(RemixIcon.XXX),
    ...
)
```

### 4. 可选的全局简化（推荐）

如果项目中有多处使用 `RemixIcon`，可以添加一个辅助函数：

```kotlin
// 放在项目公共 Composable 工具中
@Composable
fun RemixIcon(
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

然后直接写：
```kotlin
RemixIcon(RemixIcon.Media.CameraFill, modifier = Modifier.size(24.dp))
```

### 5. 确保依赖 CMP Resources

下游项目需要添加 `compose.components.resources` 依赖。在 KMP 项目的 `commonMain` 中：

```kotlin
commonMain.dependencies {
    // 已有
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    
    // 可能需要添加（如尚未引入）
    implementation(compose.components.resources)
}
```

### 6. 检查 XML 资源冲突

如果下游项目也在使用 `composeResources`（声明了 `Res` 类），可能有包名冲突。确保两个模块的 `compose.resources` 配置不冲突。

## 回退方案

如果迁移过程中遇到问题需要临时回退到 v0.0.2：

```kotlin
implementation("io.github.cy745:remixicon-kmp:0.0.2")
```

但 v0.0.2 的旧代码生成方式已不再维护，建议尽快迁移到 v0.0.3。

## 验证清单

- [ ] 依赖版本已更新到 `0.0.3`
- [ ] 所有 `import com.lalilu.RemixIcon` 引用仍然有效
- [ ] 所有 `ImageVector` 类型声明已替换为 `DrawableResource`
- [ ] 所有 Composable 渲染处已添加 `painterResource()` 或 `vectorResource()`
- [ ] 项目能正常编译通过
- [ ] 图标在运行时代显示正常
