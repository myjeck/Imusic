/*
 * ArchiveTune (2026)
 * © Rukamori — github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package moe.rukamori.archivetune.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import moe.rukamori.archivetune.LocalPlayerConnection
import moe.rukamori.archivetune.constants.MiniPlayerHeight
import moe.rukamori.archivetune.constants.NavigationBarMaxWidth
import moe.rukamori.archivetune.constants.SwipeSensitivityKey
import moe.rukamori.archivetune.constants.SwipeThumbnailKey
import moe.rukamori.archivetune.utils.rememberPreference
import kotlin.math.roundToInt

@Composable
fun MiniPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier = Modifier,
    pureBlack: Boolean,
    isPairedWithNavigation: Boolean = false,
    hazeState: HazeState? = null,
) {
    NewMiniPlayer(
        position = position,
        duration = duration,
        modifier = modifier,
        pureBlack = pureBlack,
        isPairedWithNavigation = isPairedWithNavigation,
        hazeState = hazeState,
    )
}

@Composable
private fun NewMiniPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier = Modifier,
    pureBlack: Boolean,
    isPairedWithNavigation: Boolean,
    hazeState: HazeState?,
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    val layoutDirection = LocalLayoutDirection.current
    val coroutineScope = rememberCoroutineScope()
    val swipeSensitivity by rememberPreference(SwipeSensitivityKey, 0.73f)
    val swipeThumbnail by rememberPreference(SwipeThumbnailKey, true)

    val contentColors = rememberMiniPlayerContentColors()

    val miniPlayerShape =
        remember(isPairedWithNavigation) {
            if (isPairedWithNavigation) {
                RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp,
                )
            } else {
                null
            }
        } ?: MaterialTheme.shapes.extraLarge

    SwipeableMiniPlayerBox(
        modifier = modifier,
        contentMaxWidth = if (isPairedWithNavigation) NavigationBarMaxWidth else null,
        swipeSensitivity = swipeSensitivity,
        swipeThumbnail = swipeThumbnail,
        playerConnection = playerConnection,
        layoutDirection = layoutDirection,
        coroutineScope = coroutineScope,
        pureBlack = pureBlack,
        useLegacyBackground = false,
    ) { offsetX ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MiniPlayerHeight)
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .clip(miniPlayerShape)
                    .then(
                        if (hazeState != null) {
                            Modifier.hazeEffect(
                                state = hazeState,
                                style =
                                    HazeStyle(
                                        backgroundColor = MaterialTheme.colorScheme.surface,
                                        tint = HazeTint(Color.White.copy(alpha = 0.16f)),
                                        blurRadius = 26.dp,
                                        noiseFactor = 0f,
                                    ),
                            )
                        } else {
                            Modifier.background(Color.Black.copy(alpha = 0.4f))
                        },
                    ).border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.24f),
                        shape = miniPlayerShape,
                    ),
        ) {
            NewMiniPlayerContent(
                position = position,
                duration = duration,
                playerConnection = playerConnection,
                colors = contentColors,
            )
        }
    }
}

@Composable
private fun rememberMiniPlayerContentColors(): MiniPlayerContentColors =
    remember {
        MiniPlayerContentColors(
            title = Color.White,
            secondary = Color.White.copy(alpha = 0.72f),
            progress = Color.White,
            progressTrack = Color.White.copy(alpha = 0.24f),
            artworkContainer = Color.White.copy(alpha = 0.14f),
            artworkBorder = Color.White.copy(alpha = 0.22f),
            primaryButtonContainer = Color.White.copy(alpha = 0.92f),
            primaryButtonIcon = Color.Black,
            secondaryButtonContainer = Color.Black.copy(alpha = 0.22f),
            buttonIcon = Color.White,
            disabledButtonIcon = Color.White.copy(alpha = 0.38f),
            togetherContainer = Color.White.copy(alpha = 0.16f),
            togetherContent = Color.White,
        )
    }
