package se.supernovait.anya.core.presentation.common.list

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_swipeable_list_item_with_actions
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun SwipeableListItemWithActions(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    state: SwipeableListItemState = rememberSwipeableListItemState(),
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val a11yText = stringResource(Res.string.a11y_swipeable_list_item_with_actions)
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.isActionsRevealed, state.contextMenuWidth) {
        if(state.isActionsRevealed) {
            offset.animateTo(state.contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .semantics {
            this.contentDescription = if (contentDescription.isNullOrBlank()) a11yText else contentDescription
            role = Role.Button
        }
    ) {
        Row(modifier = Modifier
            .onSizeChanged {
                state.contextMenuWidth(it.width.toFloat())
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offset.value.roundToInt(), 0) }
            .pointerInput(state.contextMenuWidth) {
                detectHorizontalDragGestures(onHorizontalDrag = { _, dragAmount ->
                    scope.launch {
                        val newOffset = (offset.value + dragAmount).coerceIn(0f, state.contextMenuWidth)
                        offset.snapTo(newOffset)
                    }
                },
                    onDragEnd = {
                        when {
                            offset.value >= state.contextMenuWidth / 2f -> {
                                scope.launch {
                                    offset.animateTo(state.contextMenuWidth)
                                    onExpanded()
                                }
                            }
                            else -> {
                                scope.launch {
                                    offset.animateTo(0f)
                                    onCollapsed()
                                }
                            }
                        }
                    }
                )
            }
        ) {
            content()
        }
    }
}
