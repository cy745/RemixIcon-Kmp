package com.lalilu

import br.com.devsrsouza.svg2compose.RemixIconResourceGenerator
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class RemixIconResourceGeneratorTest {

    @Test
    fun `converts SVG filename to resource name with category prefix`() {
        val entries = generateTestEntries(setOf(
            "Buildings" to listOf("ancient-gate-fill.svg", "bank-line.svg"),
        ))

        val ancientGate = entries.find { it.svgFileName == "ancient-gate-fill.svg" }
        assertNotNull(ancientGate)
        assertEquals("buildings_ancient_gate_fill", ancientGate!!.resourceName)
        assertEquals("ancientGateFill", ancientGate.propertyName)
        assertEquals("Buildings", ancientGate.categoryName)

        val bankLine = entries.find { it.svgFileName == "bank-line.svg" }
        assertNotNull(bankLine)
        assertEquals("buildings_bank_line", bankLine!!.resourceName)
        assertEquals("bankLine", bankLine.propertyName)
    }

    @Test
    fun `handles ampersand and spaces in category`() {
        val entries = generateTestEntries(setOf(
            "Health & Medical" to listOf("heart-fill.svg"),
        ))

        val heartFill = entries.find { it.svgFileName == "heart-fill.svg" }
        assertNotNull(heartFill)
        assertEquals("health_and_medical_heart_fill", heartFill!!.resourceName)
        assertEquals("heartFill", heartFill.propertyName)
        assertEquals("HealthAndMedical", heartFill.categoryName)
    }

    @Test
    fun `handles spaces in category name`() {
        val entries = generateTestEntries(setOf(
            "User & Faces" to listOf("user-smile-fill.svg"),
        ))

        val userSmile = entries.find { it.svgFileName == "user-smile-fill.svg" }
        assertNotNull(userSmile)
        assertEquals("user_and_faces_user_smile_fill", userSmile!!.resourceName)
        assertEquals("userSmileFill", userSmile.propertyName)
        assertEquals("UserAndFaces", userSmile.categoryName)
    }

    @Test
    fun `prefixes property with underscore when starts with digit`() {
        val entries = generateTestEntries(setOf(
            "Media" to listOf("24-hours-fill.svg"),
        ))

        val hours24 = entries.find { it.svgFileName == "24-hours-fill.svg" }
        assertNotNull(hours24)
        assertEquals("media_24_hours_fill", hours24!!.resourceName)
        assertEquals("_24HoursFill", hours24.propertyName)
    }

    @Test
    fun `all category names are valid Kotlin identifiers`() {
        val entries = generateTestEntries(setOf(
            "Buildings" to listOf("bank-fill.svg"),
            "Health & Medical" to listOf("heart-fill.svg"),
            "User & Faces" to listOf("user-fill.svg"),
        ))

        entries.map { it.categoryName }.distinct().forEach { name ->
            assertTrue(
                "Category '$name' is not a valid Kotlin identifier",
                name.matches(Regex("^[A-Za-z_][A-Za-z0-9_]*$"))
            )
        }
    }

    @Test
    fun `all property names are valid Kotlin identifiers`() {
        val entries = generateTestEntries(setOf(
            "Buildings" to listOf("bank-fill.svg", "bank-line.svg"),
            "Media" to listOf("24-hours-fill.svg"),
        ))

        entries.forEach { entry ->
            assertTrue(
                "Property '${entry.propertyName}' is not a valid Kotlin identifier",
                entry.propertyName.matches(Regex("^[A-Za-z_][A-Za-z0-9_]*$"))
            )
        }
    }

    @Test
    fun `resource names are unique across all categories`() {
        val entries = generateTestEntries(setOf(
            "Buildings" to listOf("bank-fill.svg"),
            "Business" to listOf("bank-fill.svg"),
        ))
        // Same icon name in different categories must produce unique resource names

        val bankFillBuildings = entries.find { it.categoryName == "Buildings" }
        val bankFillBusiness = entries.find { it.categoryName == "Business" }
        assertNotNull(bankFillBuildings)
        assertNotNull(bankFillBusiness)
        assertNotEquals(bankFillBuildings!!.resourceName, bankFillBusiness!!.resourceName)
        assertEquals("buildings_bank_fill", bankFillBuildings.resourceName)
        assertEquals("business_bank_fill", bankFillBusiness!!.resourceName)
    }

    @Test
    fun `output XML files are well-formed`() {
        val tempSvgDir = kotlin.io.path.createTempDirectory("test").toFile()
        val tempOutputDir = kotlin.io.path.createTempDirectory("test").toFile()

        try {
            val catDir = File(tempSvgDir, "Buildings")
            catDir.mkdirs()
            File(catDir, "test-icon.svg").writeText(
                """<svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M12 2L2 22h20L12 2z"/></svg>"""
            )

            val entries = RemixIconResourceGenerator.convertSvgToDrawableResources(
                svgRootDir = tempSvgDir,
                outputDir = tempOutputDir,
            )

            assertEquals(1, entries.size)
            val xmlFile = entries.first().xmlFile
            assertTrue("XML file should exist", xmlFile.exists())
            assertTrue("XML file should be non-empty", xmlFile.length() > 0)

            val xmlContent = xmlFile.readText()
            assertTrue("XML should contain <vector>", xmlContent.contains("<vector"))
            assertTrue("XML should contain pathData", xmlContent.contains("android:pathData"))
        } finally {
            tempSvgDir.deleteRecursively()
            tempOutputDir.deleteRecursively()
        }
    }

    // --- helpers ---

    private fun generateTestEntries(
        categories: Set<Pair<String, List<String>>>,
    ): List<RemixIconResourceGenerator.IconResourceEntry> {
        val tempSvgDir = kotlin.io.path.createTempDirectory("test").toFile()
        val tempOutputDir = kotlin.io.path.createTempDirectory("test").toFile()

        try {
            val svgContent = """<svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z"/></svg>"""

            categories.forEach { (category, iconNames) ->
                val catDir = File(tempSvgDir, category)
                catDir.mkdirs()
                iconNames.forEach { name ->
                    File(catDir, name).writeText(svgContent)
                }
            }

            return RemixIconResourceGenerator.convertSvgToDrawableResources(
                svgRootDir = tempSvgDir,
                outputDir = tempOutputDir,
            )
        } finally {
            tempSvgDir.deleteRecursively()
            tempOutputDir.deleteRecursively()
        }
    }
}
