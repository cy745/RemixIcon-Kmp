package br.com.devsrsouza.svg2compose

import com.android.ide.common.vectordrawable.Svg2Vector
import java.io.File

/**
 * Converts RemixIcon SVG files to Android Vector XML drawables
 * and outputs them to the CMP composeResources/drawable/ directory.
 */
object RemixIconResourceGenerator {

    /**
     * Converts all SVG icons to Android Vector XML files.
     *
     * @param svgRootDir the RemixIcon/icons directory containing category subdirectories
     * @param outputDir the composeResources/drawable/ output directory
     * @return list of generated resource entries with metadata
     */
    fun convertSvgToDrawableResources(
        svgRootDir: File,
        outputDir: File,
    ): List<IconResourceEntry> {
        if (!svgRootDir.exists()) {
            error("SVG root directory does not exist: $svgRootDir")
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val entries = mutableListOf<IconResourceEntry>()

        svgRootDir.listFiles()
            ?.filter { it.isDirectory }
            ?.sortedBy { it.name }
            ?.forEach { categoryDir ->
                val categoryName = categoryDir.name
                    .trim()
                    .replace("&", "And")
                    .replace(' ', '_')

                categoryDir.listFiles()
                    ?.filter { it.extension.equals("svg", ignoreCase = true) }
                    ?.sortedBy { it.name }
                    ?.forEach { svgFile ->
                        val iconName = svgFile.nameWithoutExtension
                        val entry = convertSingleSvg(
                            svgFile = svgFile,
                            categoryName = categoryName,
                            outputDir = outputDir,
                        )
                        entries.add(entry)
                    }
            }

        return entries
    }

    /**
     * Converts a single SVG file to Android Vector XML.
     */
    fun convertSingleSvg(
        svgFile: File,
        categoryName: String,
        outputDir: File,
    ): IconResourceEntry {
        val iconBaseName = svgFile.nameWithoutExtension

        // Resource name: "buildings_ancient_gate_fill"
        val resourceName = "${categoryName.lowercase()}_${iconBaseName
            .replace('-', '_')
            .replace(' ', '_')
            .lowercase()}"

        val xmlFileName = "$resourceName.xml"
        val xmlOutputFile = File(outputDir, xmlFileName)

        // Convert SVG → Android Vector XML
        val outputStream = xmlOutputFile.outputStream()
        try {
            Svg2Vector.parseSvgToXml(svgFile, outputStream)
        } finally {
            outputStream.close()
        }

        // Kotlin property name: "AncientGateFill"
        val propertyName = iconBaseName
            .replace('-', '_')
            .let { name ->
                name.split('_').joinToString("") { part ->
                    part.replaceFirstChar { it.uppercase() }
                }
            }
            .let { name ->
                if (name.first().isDigit()) "_$name" else name
            }

        return IconResourceEntry(
            categoryName = categoryName
                .replace(' ', '_')
                .split('_').joinToString("") { it.replaceFirstChar { c -> c.uppercase() } },
            svgFileName = svgFile.name,
            resourceName = resourceName,
            propertyName = propertyName,
            xmlFile = xmlOutputFile,
        )
    }

    data class IconResourceEntry(
        val categoryName: String,
        val svgFileName: String,
        val resourceName: String,
        val propertyName: String,
        val xmlFile: File,
    )
}
