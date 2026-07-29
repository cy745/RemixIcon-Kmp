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
    fun `RemixIcon object exists`() {
        assertNotNull(RemixIcon)
    }

    @Test
    fun `all known categories have icons`() {
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
    fun `AllIcons is not empty`() {
        assertTrue(RemixIcon.AllIcons.isNotEmpty(), "AllIcons should not be empty")
    }

    @Test
    fun `all icons in AllIcons are non-null DrawableResource`() {
        RemixIcon.AllIcons.forEachIndexed { index, icon ->
            assertNotNull(icon, "Icon at index $index in AllIcons is null")
        }
    }

    @Test
    fun `Count matches AllIcons size`() {
        assertTrue(RemixIcon.Count > 0, "Count should be > 0")
        assertTrue(
            RemixIcon.Count == RemixIcon.AllIcons.size,
            "Count (${RemixIcon.Count}) != AllIcons.size (${RemixIcon.AllIcons.size})"
        )
    }

    @Test
    fun `AllIcons contains first icon`() {
        assertNotNull(RemixIcon.AllIcons.first())
    }

    @Test
    fun `icon property is typed as DrawableResource`() {
        val icon: DrawableResource = RemixIcon.Buildings.AncientGateFill
        assertNotNull(icon)
    }
}
