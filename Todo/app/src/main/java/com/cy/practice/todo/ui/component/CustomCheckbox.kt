package com.cy.practice.todo.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cy.practice.todo.ui.theme.TodoTheme


@Composable
fun CustomCheckbox(
    isChecked: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 24,
    shape: Shape = CircleShape,
    colors: CheckboxColors = defaultCheckboxColors()
) {
    val transition = updateTransition(targetState = isChecked, label = "Check Transition")

    val backgroundColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 300, easing = FastOutSlowInEasing) },
        label = "Background Color"
    ) { checked ->
        if (checked) colors.checkedBackground else colors.uncheckedBackground
    }

    val scale by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        },
        label = "Scale"
    ) { if (it) 1.1f else 0.8f }

    val checkScale by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = 0.3f, stiffness = Spring.StiffnessMediumLow) },
        label = "Check Scale"
    ) { if (it) 1f else 0f }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .background(color = backgroundColor, shape = shape)
            .border(
                width = 1.5.dp,
                color = colors.borderColor,
                shape = shape
            )
            .padding(4.dp)
            .toggleable(
                value = isChecked,
                role = Role.Checkbox,
                onValueChange = onValueChange
            ),
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = colors.checkMark,
            modifier = Modifier.graphicsLayer(scaleX = checkScale, scaleY = checkScale)
        )
    }
}

@Immutable
data class CheckboxColors(
    val checkedBackground: Color,
    val uncheckedBackground: Color,
    val borderColor: Color,
    val checkMark: Color
)


@Composable
fun defaultCheckboxColors(): CheckboxColors {
    return CheckboxColors(
        checkedBackground = MaterialTheme.colorScheme.primary,
        uncheckedBackground = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.primary,
        checkMark = Color.White
    )
}


@Preview(showBackground = true)
@Composable
fun CustomCheckboxPreview(modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf(false) }
    TodoTheme {
        Row {
            CustomCheckbox(state, {state = it})
            Spacer(modifier.size(8.dp))
            CustomCheckbox(
                state,
                {state = it},
                colors = defaultCheckboxColors().copy(
                    checkedBackground = Color.Transparent,
                    uncheckedBackground = Color.Transparent,
                    borderColor = MaterialTheme.colorScheme.primary,
                    checkMark = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}