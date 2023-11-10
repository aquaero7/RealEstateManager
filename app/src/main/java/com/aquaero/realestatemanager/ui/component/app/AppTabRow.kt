package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.AppDestination
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.SelectedTabColor
import java.util.Locale

@Composable
fun AppTabRow(
    modifier: Modifier = Modifier,
    allScreens: List<AppDestination>,
    onTabSelected: (AppDestination) -> Unit,
    currentScreen: AppDestination,
    colorAnimLabel: String,
) {
    Surface(
        modifier = modifier
            .height(tabHeight)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.selectableGroup(),
            //horizontalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            allScreens.forEach { screen ->
                AppTab(
                    text = screen.route,
                    icon = screen.icon,
                    onSelected = { onTabSelected(screen) },
                    selected = currentScreen == screen,
                    colorAnimLabel = colorAnimLabel,
                )
            }
        }
    }
}

@Composable
private fun AppTab(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean,
    colorAnimLabel: String,
) {
    val color = if (selected) SelectedTabColor else MaterialTheme.colorScheme.onSurface
    val durationMillis = if (selected) TAB_FADE_IN_ANIMATION_DURATION else TAB_FADE_OUT_ANIMATION_DURATION
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = TAB_FADE_IN_ANIMATION_DELAY
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = INACTIVE_TAB_OPACITY),
        animationSpec = animSpec, label = colorAnimLabel
    )
    Row(
        modifier = modifier
            // .padding(16.dp)
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .animateContentSize()
            .height(tabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
        if (selected) {
            Spacer(Modifier.width(12.dp))
            Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
        }
    }
}

private val tabHeight = 64.dp   //56.dp
private const val TAB_FADE_IN_ANIMATION_DURATION = 150
private const val TAB_FADE_OUT_ANIMATION_DURATION = 100
private const val TAB_FADE_IN_ANIMATION_DELAY = 100
private const val INACTIVE_TAB_OPACITY = 0.60f