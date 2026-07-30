package com.lalilu

import org.jetbrains.compose.resources.DrawableResource
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Validates the generated RemixIcon wrapper code.
 */
class RemixIconValidationTest {

    @Test
    fun remixIconObjectExists() {
        assertNotNull(RemixIcon)
    }

    @Test
    fun allKnownCategoriesHaveIcons() {
        assertNotNull(RemixIcon.Arrows.arrowDownBoxFill)
        assertNotNull(RemixIcon.Buildings.ancientGateFill)
        assertNotNull(RemixIcon.Business.advertisementFill)
        assertNotNull(RemixIcon.Communication.chat1Fill)
        assertNotNull(RemixIcon.Design.alignItemBottomFill)
        assertNotNull(RemixIcon.Development.bracesFill)
        assertNotNull(RemixIcon.Device.airplayFill)
        assertNotNull(RemixIcon.Document.articleFill)
        assertNotNull(RemixIcon.Editor.aB)
        assertNotNull(RemixIcon.Finance.auctionFill)
        assertNotNull(RemixIcon.Food.beerFill)
        assertNotNull(RemixIcon.HealthAndMedical.aedElectrodesFill)
        assertNotNull(RemixIcon.Logos.alibabaCloudFill)
        assertNotNull(RemixIcon.Map.anchorFill)
        assertNotNull(RemixIcon.Media.albumFill)
        assertNotNull(RemixIcon.Others.accessibilityFill)
        assertNotNull(RemixIcon.System.addBoxFill)
        assertNotNull(RemixIcon.UserAndFaces.accountBox2Fill)
        assertNotNull(RemixIcon.Weather.blazeFill)
    }

    @Test
    fun allIconsIsNotEmpty() {
        assertTrue(RemixIcon.AllIcons.isNotEmpty(), "AllIcons should not be empty")
    }

    @Test
    fun allIconsAreNonNullDrawableResource() {
        RemixIcon.AllIcons.forEachIndexed { index, icon ->
            assertNotNull(icon, "Icon at index $index in AllIcons is null")
        }
    }

    @Test
    fun countMatchesAllIconsSize() {
        assertTrue(RemixIcon.Count > 0, "Count should be > 0")
        assertTrue(
            RemixIcon.Count == RemixIcon.AllIcons.size,
            "Count (${RemixIcon.Count}) != AllIcons.size (${RemixIcon.AllIcons.size})"
        )
    }

    @Test
    fun allIconsContainsFirstIcon() {
        assertNotNull(RemixIcon.AllIcons.first())
    }

    @Test
    fun iconPropertyIsTypedAsDrawableResource() {
        val icon: DrawableResource = RemixIcon.Buildings.ancientGateFill
        assertNotNull(icon)
    }
}
