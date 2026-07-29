package br.com.devsrsouza.svg2compose

import br.com.devsrsouza.svg_to_compose.BuildConfig
import java.io.File

// Entry point for the SVG-to-Resource transformation.
// 1. Converts all RemixIcon SVGs to Android Vector XML drawables
// 2. Generates lightweight RemixIcon.kt wrapper code
fun main() {
    val root = File(BuildConfig.rootPath)
    val svgRootDir = File(root, "RemixIcon/icons")
    val resourceOutputDir = File(root, "core/src/commonMain/composeResources/drawable")
    val wrapperOutputDir = File(root, "core/src/commonMain/kotlin")

    println("=== RemixIcon Resource Transformation ===")
    println("SVG root:       $svgRootDir")
    println("Resource out:   $resourceOutputDir")
    println("Wrapper out:    $wrapperOutputDir")
    println()

    // Step 1: Convert all SVGs to Android Vector XML drawables
    println("[1/2] Converting SVGs to Android Vector XML...")
    val entries = RemixIconResourceGenerator.convertSvgToDrawableResources(
        svgRootDir = svgRootDir,
        outputDir = resourceOutputDir,
    )
    println("      " + entries.size + " icons converted")
    val categories = entries.map { it.categoryName }.distinct().sorted()
    println("      " + categories.size + " categories: " + categories.joinToString(", "))

    // Step 2: Generate lightweight RemixIcon.kt wrapper
    println("[2/2] Generating RemixIcon.kt wrapper...")
    RemixIconWrapperGenerator.generate(
        entries = entries,
        outputDir = wrapperOutputDir,
    )
    println("      RemixIcon.kt generated with " + categories.size + " category objects")
    println()

    // Summary
    println("=== Summary ===")
    println("SVG files:     " + entries.size)
    println("Categories:    " + categories.size)
    println("XML resources: " + File(resourceOutputDir, "").walkTopDown().count { it.extension == "xml" })
    println("Wrapper:       RemixIcon.kt")
}
