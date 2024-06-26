package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmail.luizjmfilho.sevenwonders.R
import com.gmail.luizjmfilho.sevenwonders.ui.theme.SevenWondersTheme

const val homeScreenTestTag: String = "Home Screen"

@Composable
fun HomeScreen(
    onCriarPartidaClick: () -> Unit,
    onMatchesHistoryClick: () -> Unit,
    onStatsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onScienceSimulatorClick: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
) {
    WithLifecycleOwner(homeViewModel)

    HomeScreen(
        onCriarPartidaClick = onCriarPartidaClick,
        onMatchesHistoryClick = onMatchesHistoryClick,
        onStatsClick = onStatsClick,
        onAboutClick = onAboutClick,
        onScienceSimulatorClick = onScienceSimulatorClick,
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    onCriarPartidaClick: () -> Unit,
    onMatchesHistoryClick: () -> Unit,
    onStatsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onScienceSimulatorClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = Modifier
            .testTag(homeScreenTestTag)
    ) { scaffoldPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = if (isSystemInDarkTheme()) {
                    painterResource(id = R.drawable.fundohomescreendark)
                }  else {
                    painterResource(id = R.drawable.fundohomescreen)
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = if (isSystemInDarkTheme()) painterResource(id = R.drawable.logodarkk) else painterResource(id = R.drawable.logolight),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                )
                Spacer(Modifier.weight(0.40f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .fillMaxHeight()
                ) {
                    HomeScreenButton(
                        onClick = onCriarPartidaClick,
                        textinho = stringResource(R.string.criar_partida_button),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    HomeScreenButton(
                        onClick = onStatsClick,
                        textinho = stringResource(R.string.estatisticas_button),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    HomeScreenButton(
                        onClick = onMatchesHistoryClick,
                        textinho = stringResource(R.string.historico_de_partidas_button),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    HomeScreenButton(
                        onClick = onScienceSimulatorClick,
                        textinho = stringResource(R.string.science_points_simulator),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(Modifier.weight(0.60f))
                Row(
                    modifier = Modifier
                        .padding(bottom = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        onClick = onAboutClick
                    ) {
                        Text(text = stringResource(R.string.About))
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenButton(
    onClick: () -> Unit,
    textinho: String,
    modifier: Modifier = Modifier,
){
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = textinho,
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SevenWondersTheme {
        HomeScreen(
            onCriarPartidaClick = {},
            onMatchesHistoryClick = {},
            onStatsClick = {},
            onAboutClick = {},
            onScienceSimulatorClick = {}
        )
    }
}


