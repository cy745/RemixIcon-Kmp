package br.com.devsrsouza.svg2compose

import org.junit.Test
import java.io.File

class EmojiTest {
    @Test
    fun test() {
        val iconTest = File("./")

        println(iconTest.path)
//    val src = File("/Users/marioferreiravilanova/Documents/Workspace/kotlin/svg-to-compose/src/test/results").apply { mkdirs() }
//
//    Svg2Compose.parse(
//        applicationIconPackage = "com.test",
//        accessorName = "Icons",
//        outputSourceDirectory = src,
//        vectorsDirectory = iconTest,
//        type = VectorType.SVG,
//        iconNameTransformer = { name, group ->
//            name.split("-").joinToString(separator = "").removePrefix(group)
//        },
//        allAssetsPropertyName = "AllIcons",
//        generatePreview = true,
//    )
    }
}

