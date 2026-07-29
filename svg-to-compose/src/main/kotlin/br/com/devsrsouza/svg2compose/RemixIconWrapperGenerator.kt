package br.com.devsrsouza.svg2compose

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

/**
 * Generates the lightweight RemixIcon.kt wrapper that exposes
 * [org.jetbrains.compose.resources.DrawableResource] references organized by category.
 *
 * Generated structure:
 * ```
 * @file:OptIn(InternalResourceApi::class)
 * package ...
 *
 * object RemixIcon {
 *     object Arrows { val Xxx: DrawableResource = DrawableResource(...) }
 *     object Buildings { ... }
 *     ...
 *     val AllIcons: List<DrawableResource> = listOf(...)
 *     val Count: Int = AllIcons.size
 * }
 * ```
 */
object RemixIconWrapperGenerator {

    private val drawableResourceType =
        ClassName("org.jetbrains.compose.resources", "DrawableResource")
    private val resourceItemType =
        ClassName("org.jetbrains.compose.resources", "ResourceItem")
    private val listType = List::class.asClassName()
    private val internalApiAnnotation =
        ClassName("org.jetbrains.compose.resources", "InternalResourceApi")
    private val optInClass = ClassName("kotlin", "OptIn")

    private val resourceBasePath = "composeResources/com.lalilu.remixicon.generated.resources"

    fun generate(
        entries: List<RemixIconResourceGenerator.IconResourceEntry>,
        outputDir: File,
        packageName: String = "com.lalilu",
    ) {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val entriesByCategory = entries.groupBy { it.categoryName }
            .toSortedMap()

        val fileSpec = buildRemixIconFile(packageName, entriesByCategory)
        fileSpec.writeTo(outputDir)
    }

    private fun buildRemixIconFile(
        packageName: String,
        entriesByCategory: Map<String, List<RemixIconResourceGenerator.IconResourceEntry>>,
    ): FileSpec {
        val allCategoryNames = mutableListOf<String>()
        val allIconProperties = mutableListOf<String>()

        val remixIconBuilder = TypeSpec.objectBuilder("RemixIcon")

        entriesByCategory.forEach { (categoryName, categoryEntries) ->
            val categoryObject = buildCategoryObject(categoryName, categoryEntries)
            remixIconBuilder.addType(categoryObject)

            allCategoryNames.add(categoryName)
            categoryEntries.forEach { entry ->
                allIconProperties.add("${categoryName}.${entry.propertyName}")
            }
        }

        // AllIcons: List<DrawableResource>
        val allIconsType = listType.parameterizedBy(drawableResourceType)
        val allRefs = allIconProperties.joinToString(", ")
        val allIconsProperty = PropertySpec.builder("AllIcons", allIconsType)
            .getter(FunSpec.getterBuilder()
                .addStatement("return listOf(%L)", allRefs)
                .build())
            .build()
        remixIconBuilder.addProperty(allIconsProperty)

        // Count: Int
        val countProperty = PropertySpec.builder("Count", Int::class)
            .getter(FunSpec.getterBuilder()
                .addStatement("return AllIcons.size")
                .build())
            .build()
        remixIconBuilder.addProperty(countProperty)

        // Build the file
        val builder = FileSpec.builder(packageName, "RemixIcon")
            .addAnnotation(
                AnnotationSpec.builder(optInClass)
                    .addMember("%T::class", internalApiAnnotation)
                    .build()
            )
            .addType(remixIconBuilder.build())

        return builder.build()
    }

    private fun buildCategoryObject(
        categoryName: String,
        entries: List<RemixIconResourceGenerator.IconResourceEntry>,
    ): TypeSpec {
        val objectBuilder = TypeSpec.objectBuilder(categoryName)

        entries.sortedBy { it.propertyName }.forEach { entry ->
            val property = PropertySpec.builder(entry.propertyName, drawableResourceType)
                .initializer(
                    "%T(%S, setOf(%T(setOf(), %S, -1, -1)))",
                    drawableResourceType,
                    "drawable:${entry.resourceName}",
                    resourceItemType,
                    "$resourceBasePath/drawable/${entry.resourceName}.xml"
                )
                .build()
            objectBuilder.addProperty(property)
        }

        return objectBuilder.build()
    }
}
