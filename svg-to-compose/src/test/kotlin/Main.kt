package br.com.devsrsouza.svg2compose

import br.com.devsrsouza.svg_to_compose.BuildConfig
import com.google.common.base.CaseFormat
import java.io.File

fun main() {
    BuildConfig.rootPath

    val root = File(BuildConfig.rootPath)
    val srcPath = File(root, "RemixIcon/icons")
    val targetPath = File(root, "core/src/commonMain/kotlin").apply { mkdirs() }

    Svg2Compose.parse(
        applicationIconPackage = "com.lalilu",
        accessorName = "RemixIcon",
        outputSourceDirectory = targetPath,
        vectorsDirectory = srcPath,
        type = VectorType.SVG,
        iconNameTransformer = { name, group ->
            name.removePrefix(group)
                .replace('-', '_')
                .let {
                    CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it).let { name ->
                        if (name.first().isDigit()) "_$name" else name
                    }
                }
        },
        allAssetsPropertyName = "AllIcons",
        generatePreview = true,
    )
}