package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import com.aquaero.realestatemanager.INACTIVE_TAB_OPACITY
import com.aquaero.realestatemanager.TAB_FADE_IN_ANIMATION_DELAY
import com.aquaero.realestatemanager.TAB_FADE_IN_ANIMATION_DURATION
import com.aquaero.realestatemanager.TAB_FADE_OUT_ANIMATION_DURATION
import com.aquaero.realestatemanager.TAB_HEIGHT
import com.aquaero.realestatemanager.ui.theme.White
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
            .height(TAB_HEIGHT)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .selectableGroup()
                .background(MaterialTheme.colorScheme.secondary),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            allScreens.forEach { screen ->
                AppTab(
                    text = stringResource(id = screen.labelResId),
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
    val color = White
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
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .animateContentSize()
            .height(TAB_HEIGHT)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = remember {
                    ripple(
                        bounded = false,
                        radius = Dp.Unspecified,
                        color = Color.Unspecified
                    )
                }
                // indication = LocalIndication.current
            )
            .clearAndSetSemantics { contentDescription = text },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
        if (selected) {
            Spacer(Modifier.width(12.dp))
            Text(text = text.uppercase(Locale.getDefault()), color = tabTintColor)
        }
    }
}
