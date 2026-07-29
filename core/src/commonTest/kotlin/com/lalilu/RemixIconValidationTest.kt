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
        assertNotNull(RemixIcon.Arrows.ArrowDownBoxFill)
        assertNotNull(RemixIcon.Buildings.AncientGateFill)
        assertNotNull(RemixIcon.Business.AdvertisementFill)
        assertNotNull(RemixIcon.Communication.Chat1Fill)
        assertNotNull(RemixIcon.Design.AlignItemBottomFill)
        assertNotNull(RemixIcon.Development.BracesFill)
        assertNotNull(RemixIcon.Device.AirplayFill)
        assertNotNull(RemixIcon.Document.ArticleFill)
        assertNotNull(RemixIcon.Editor.AB)
        assertNotNull(RemixIcon.Finance.AuctionFill)
        assertNotNull(RemixIcon.Food.BeerFill)
        assertNotNull(RemixIcon.HealthAndMedical.AedElectrodesFill)
        assertNotNull(RemixIcon.Logos.AlibabaCloudFill)
        assertNotNull(RemixIcon.Map.AnchorFill)
        assertNotNull(RemixIcon.Media.AlbumFill)
        assertNotNull(RemixIcon.Others.AccessibilityFill)
        assertNotNull(RemixIcon.System.AddBoxFill)
        assertNotNull(RemixIcon.UserAndFaces.AccountBox2Fill)
        assertNotNull(RemixIcon.Weather.BlazeFill)
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
        val icon: DrawableResource = RemixIcon.Buildings.AncientGateFill
        assertNotNull(icon)
    }
}
