package se.supernovait.anya.core.presentation.common.container

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefreshContainer(
    modifier: Modifier,
    refreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.pullToRefresh(
            state = refreshState,
            isRefreshing = false,
            onRefresh = onRefresh
        )
    ) {
        content.invoke()
    }
}
