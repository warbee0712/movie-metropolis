package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.HomeScreenLayout
import movie.style.AppErrorItem
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemasScreen(
    padding: PaddingValues,
    onClickCinema: (String) -> Unit,
    state: LazyListState,
    profileIcon: @Composable () -> Unit,
    viewModel: CinemasViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val location by rememberLocation()
    LaunchedEffect(location) {
        viewModel.location.value = location ?: return@LaunchedEffect
    }
    HomeScreenLayout(
        profileIcon = profileIcon,
        title = { Text(stringResource(R.string.cinemas)) }
    ) { innerPadding, behavior ->
        CinemasScreen(
            items = items,
            behavior = behavior,
            padding = innerPadding + padding,
            onClickCinema = onClickCinema,
            state = state
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CinemasScreen(
    items: Loadable<List<CinemaView>>,
    padding: PaddingValues,
    behavior: TopAppBarScrollBehavior,
    onClickCinema: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .nestedScroll(behavior.nestedScrollConnection)
            .fillMaxSize(),
        contentPadding = padding + PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = state,
        userScrollEnabled = items.isSuccess
    ) {
        items.onLoading {
            items(7) {
                CinemaItem(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }.onSuccess { items ->
            items(items, key = CinemaView::id) {
                CinemaItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 24.dp),
                    name = it.name,
                    address = it.address.immutable(),
                    city = it.city,
                    distance = it.distance,
                    onClick = { onClickCinema(it.id) }
                )
            }
        }.onFailure {
            item {
                AppErrorItem(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    error = stringResource(R.string.error_cinemas)
                )
            }
        }.onEmpty {
            item {
                CinemaItemEmpty(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        HomeScreenLayout(profileIcon = { /*TODO*/ }, title = { /*TODO*/ }) { padding, behavior ->
            CinemasScreen(
                items = Loadable.loading(),
                padding = padding,
                behavior = behavior,
                onClickCinema = {}
            )
        }
    }
}