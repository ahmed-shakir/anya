package se.supernovait.anya.core.presentation.common.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class SwipeableListItemState(contextMenuWidth: Float, isActionsRevealed: Boolean) {
    var contextMenuWidth by mutableFloatStateOf(contextMenuWidth)
        private set
    var isActionsRevealed by mutableStateOf(isActionsRevealed)
        private set

    fun contextMenuWidth(contextMenuWidth: Float) {
        this.contextMenuWidth = contextMenuWidth
    }

    fun setIsActionsRevealed(isActionsRevealed: Boolean) {
        this.isActionsRevealed = isActionsRevealed
    }
}

@Composable
fun rememberSwipeableListItemState(contextMenuWidth: Float = 0f, isActionsRevealed: Boolean = false): SwipeableListItemState {
    return rememberSaveable(saver = listSaver(
        save = {
            listOf(it.contextMenuWidth.toString(), it.isActionsRevealed.toString())
        },
        restore = { state ->
            SwipeableListItemState(state[0].toFloat(), state[1].toBoolean())
        }
    )) {
        SwipeableListItemState(contextMenuWidth, isActionsRevealed)
    }
}
