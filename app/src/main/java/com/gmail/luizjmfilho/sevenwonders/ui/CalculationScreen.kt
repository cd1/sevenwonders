package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.luizjmfilho.sevenwonders.R
import com.gmail.luizjmfilho.sevenwonders.ui.theme.SevenWondersTheme

@Composable
fun CalculationScreenPrimaria(
    onBackClick: () -> Unit,
    onConfirmNextScreen: () -> Unit,
    modifier: Modifier = Modifier,
    calculationViewModel: CalculationViewModel = hiltViewModel(),
) {
    val calculationUiState by calculationViewModel.uiState.collectAsState()
    CalculationScreenSecundaria(
        onBackClick = onBackClick,
        onNextClick = calculationViewModel::addPlayerMatchInfo,
        onConfirmNextScreen = onConfirmNextScreen,
        onDismissNextScreen = calculationViewModel::deleteMatch,
        onPreviousCategory = calculationViewModel::onPreviousCategory,
        onNextCategory = calculationViewModel::onNextCategory,
        calculationUiState = calculationUiState,
        onMinusOnePointClick = calculationViewModel::onMinusOnePointClick,
        onPlusTwoPointsClick = calculationViewModel::onPlusTwoPointsClick,
        onPlusOnePointsClick = calculationViewModel::onPlusOnePointsClick,
        onShowTotalGrid = calculationViewModel::onShowTotalGrid,
        onShowPartialGrid = calculationViewModel::onShowPartialGrid,
        onShowScienceGrid = calculationViewModel::onShowSciendGrid,
        onShowCoinGrid = calculationViewModel::onShowCoinGrid,
        onMinusOneScienceCard = calculationViewModel::onMinusOneScienceCard,
        onPlusOneScienceCard = calculationViewModel::onPlusOneScienceCard,
        onScienceGridConfirm = calculationViewModel::onScienceGridConfirm,
        onMinusOneCoinQuantity = calculationViewModel::onMinusOneCoinQuantity,
        onPlusOneCoinQuantity = calculationViewModel::onPlusOneCoinQuantity,
        onPlusTwoCoinsQuantity = calculationViewModel::onPlusTwoCoinsQuantity,
        onCoinGridConfirm = calculationViewModel::onCoinGridConfirm,
        modifier = modifier,
    )
}

@Composable
fun CalculationScreenSecundaria(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onConfirmNextScreen: () -> Unit,
    onDismissNextScreen: () -> Unit,
    onPreviousCategory: () -> Unit,
    onNextCategory: () -> Unit,
    calculationUiState: CalculationUiState,
    onMinusOnePointClick: (Int) -> Unit,
    onPlusTwoPointsClick: (Int) -> Unit,
    onPlusOnePointsClick: (Int) -> Unit,
    onShowTotalGrid: () -> Unit,
    onShowPartialGrid: () -> Unit,
    onShowScienceGrid: () -> Unit,
    onShowCoinGrid: (Int, Int, Int) -> Unit,
    onMinusOneScienceCard: (Int) -> Unit,
    onPlusOneScienceCard: (Int) -> Unit,
    onScienceGridConfirm: (String) -> Unit,
    onMinusOneCoinQuantity: (Int) -> Unit,
    onPlusOneCoinQuantity: (Int) -> Unit,
    onPlusTwoCoinsQuantity: (Int) -> Unit,
    onCoinGridConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold (
        topBar = {
            SevenWondersAppBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.calculation_top_bar)
            )
        },
        modifier = modifier
            .testTag(newGameScreenTestTag),
    ) { scaffoldPadding ->
        var alertDialogShown by rememberSaveable { mutableStateOf(false)}
        var playerScienceOrCoinIndexBeingSelected by rememberSaveable { mutableIntStateOf(0)}
        Box(
            modifier = modifier
                .padding(scaffoldPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.fundoe),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .border(1.dp, Color.Gray)
                    .fillMaxSize(),
                contentAlignment = Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Center
                    ) {
                        when (calculationUiState.subScreen) {
                            CalculationSubScreen.ParcialGrid -> {
                                ScoringGrid(
                                    nicknameList = calculationUiState.playersList,
                                    currentCategory = calculationUiState.currentCategory,
                                    modifier = Modifier
                                        .padding(10.dp),
                                    onPreviousCategory = onPreviousCategory,
                                    onNextCategory = onNextCategory,
                                    calculationUiState = calculationUiState,
                                    onMinusOnePointClick = onMinusOnePointClick,
                                    onPlusTwoPointsClick = onPlusTwoPointsClick,
                                    onShowTotalGrid = onShowTotalGrid,
                                    onShowScienceGrid = { playerIndexBeingSelected ->
                                        playerScienceOrCoinIndexBeingSelected = playerIndexBeingSelected
                                        onShowScienceGrid()
                                    },
                                    onShowCoinGrid = { playerIndexBeingSelected, coinsQuantity, coinsScore ->
                                        playerScienceOrCoinIndexBeingSelected = playerIndexBeingSelected
                                        onShowCoinGrid(playerIndexBeingSelected, coinsQuantity, coinsScore)
                                    },
                                    onPlusOnePointsClick = onPlusOnePointsClick
                                )
                            }
                            CalculationSubScreen.TotalGrid -> {
                                TotalScoringGrid(
                                    nicknameList = calculationUiState.playersList,
                                    calculationUiState = calculationUiState,
                                    onShowParcialGrid = onShowPartialGrid,
                                )
                            }
                            CalculationSubScreen.ScienceGrid -> {
                                ScienceGrid(
                                    playerShown = calculationUiState.playersList[playerScienceOrCoinIndexBeingSelected],
                                    calculationUiState = calculationUiState,
                                    onMinusOneScienceCard = onMinusOneScienceCard,
                                    onPlusOneScienceCard = onPlusOneScienceCard,
                                    onShowPartialGrid = onShowPartialGrid,
                                    onScienceGridConfirm = { onScienceGridConfirm(calculationUiState.playersList[playerScienceOrCoinIndexBeingSelected]) }
                                )
                            }
                            CalculationSubScreen.CoinGrid -> {
                                CoinGrid(
                                    playerShown = calculationUiState.playersList[playerScienceOrCoinIndexBeingSelected],
                                    calculationUiState = calculationUiState,
                                    onMinusOneCoinQuantity = onMinusOneCoinQuantity,
                                    onPlusOneCoinQuantity = onPlusOneCoinQuantity,
                                    onPlusTwoCoinsQuantity = onPlusTwoCoinsQuantity,
                                    onShowPartialGrid = onShowPartialGrid,
                                    onCoinGridConfirm = { playerNickname ->
                                        onCoinGridConfirm(playerNickname)
                                    },
                                )
                            }
                        }
                    }
                    Row {
                        Spacer(Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                onNextClick()
                                alertDialogShown = true
                            },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(text = stringResource(R.string.generic_advance_button).uppercase())
                                Icon(
                                    imageVector = Icons.Filled.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    if (alertDialogShown) {
                        AlertDialog(
                            onDismissRequest = {},
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        alertDialogShown = false
                                        onConfirmNextScreen()
                                    },
                                ) {
                                    Text(text = stringResource(R.string.generic_confirm_text))
                                }
                            },
                            text = { Text(text = stringResource(R.string.calculation_screen_alert_dialog_text)) },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        onDismissNextScreen()
                                        alertDialogShown = false
                                    }
                                ) {
                                    Text(text = stringResource(R.string.generic_cancel_text))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: PointCategory,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(0.dp, 8.dp, 0.dp, 0.dp),
        modifier = modifier
            .height(40.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxHeight(),
        ) {
            Text(
                text = convertPointCategoryToString(category).uppercase(),
                color = Color(0xFF000080),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Image(
                painter = identifyIconOfCategory(category, false),
                contentDescription = null,
                modifier = Modifier
                    .height(30.dp)
            )
        }
    }
}

@Composable
fun IconCategoryCard(
    category: PointCategory,
    lastCardInTheGrid: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(
            0.dp,
            topEnd = if(lastCardInTheGrid) {
                8.dp
            } else {
                0.dp
            },
            0.dp,
            0.dp
        ),
        modifier = modifier
            .height(40.dp)
            .padding(1.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Center
        ) {
            Image(
                painter = identifyIconOfCategory(category, true),
                contentDescription = null,
                modifier = Modifier
                    .height(30.dp),
            )
        }
    }
}


@Composable
fun identifyIconOfCategory(
    category: PointCategory,
    semiWarIcon: Boolean,
): Painter {
    return when (category) {
        PointCategory.WonderBoard -> painterResource(id = R.drawable.wonder_icon)
        PointCategory.Coin -> painterResource(id = R.drawable.coin_icon)
        PointCategory.War -> if (semiWarIcon) {
            painterResource(id = R.drawable.semiwar_icon)
        }  else {
            painterResource(id = R.drawable.war_icon)
        }
        PointCategory.BlueCard -> painterResource(id = R.drawable.bluecard_icon)
        PointCategory.YellowCard -> painterResource(id = R.drawable.yellowcard_icon)
        PointCategory.GreenCard -> painterResource(id = R.drawable.greencard_icon)
        PointCategory.PurpleCard -> painterResource(id = R.drawable.purplecard_icon)
    }
}

@Composable
fun convertPointCategoryToString(
    category: PointCategory,
): String {
    return when (category) {
        PointCategory.WonderBoard -> stringResource(R.string.point_category_wonder)
        PointCategory.Coin -> stringResource(R.string.point_category_coin)
        PointCategory.War -> stringResource(R.string.point_category_war)
        PointCategory.BlueCard -> stringResource(R.string.point_category_blue_card)
        PointCategory.YellowCard -> stringResource(R.string.point_category_yellow_card)
        PointCategory.GreenCard -> stringResource(R.string.point_category_green_card)
        PointCategory.PurpleCard -> stringResource(R.string.point_category_purple_card)
    }
}

@Composable
fun NicknameCard(
    nickname: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp),
        modifier = modifier
            .widthIn(max = 90.dp),
        colors = CardDefaults.cardColors(Color(0xFFF3ECCF)),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                text = nickname,
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth(),
                fontStyle = FontStyle.Italic,
                color = Color(0xFF50504F),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ScoringGrid(
    nicknameList: List<String>,
    currentCategory: PointCategory,
    onPreviousCategory: () -> Unit,
    onNextCategory: () -> Unit,
    calculationUiState: CalculationUiState,
    onMinusOnePointClick: (Int) -> Unit,
    onPlusTwoPointsClick: (Int) -> Unit,
    onPlusOnePointsClick: (Int) -> Unit,
    onShowTotalGrid: () -> Unit,
    onShowScienceGrid: (Int) -> Unit,
    onShowCoinGrid: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spaceBetweenCards = 3.dp
    val cardsHeight = 56.dp
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .height(IntrinsicSize.Max)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spaceBetweenCards),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                for (player in nicknameList) {
                    NicknameCard(
                        nickname = player,
                        modifier = Modifier
                            .height(cardsHeight)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(spaceBetweenCards),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 0.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .height(40.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.general_socring_acronym).uppercase(),
                            modifier = Modifier
                                .padding(3.dp),
                            color = Color(0xFF000080)
                        )
                    }
                }
                for (player in nicknameList) {
                    Card(
                        shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .height(cardsHeight)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = calculationUiState.totalScoreList[nicknameList.indexOf(player)].toString(),
                                modifier = Modifier
                                    .padding(3.dp)
                                    .fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000080),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(spaceBetweenCards),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                CategoryCard(
                    category = currentCategory,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                for (player in nicknameList) {
                    Card(
                        shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .height(cardsHeight)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color(0xFFF3ECCF)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (currentCategory != PointCategory.GreenCard && currentCategory != PointCategory.Coin) {
                                IconButton(
                                    onClick = { onMinusOnePointClick(nicknameList.indexOf(player)) }
                                ) {
                                    Text(
                                        text = stringResource(R.string.minus_one_point),
                                        color = Color(0xFFA2A0A0),
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                                Text(
                                    text = when (currentCategory) {
                                        PointCategory.WonderBoard -> calculationUiState.wonderBoardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        PointCategory.War -> calculationUiState.warScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        PointCategory.BlueCard -> calculationUiState.blueCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        PointCategory.YellowCard -> calculationUiState.yellowCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        PointCategory.PurpleCard -> calculationUiState.purpleCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        else -> "" // esse caso em teoria nunca vai acontecer
                                    },
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(start = 5.dp, end = 5.dp)
                                )
                                IconButton(
                                    onClick = { onPlusOnePointsClick(nicknameList.indexOf(player)) }
                                ) {
                                    Text(
                                        text = stringResource(R.string.plus_one_point),
                                        color = Color(0xFFA2A0A0),
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                                IconButton(
                                    onClick = { onPlusTwoPointsClick(nicknameList.indexOf(player)) }
                                ) {
                                    Text(
                                        text = stringResource(R.string.plus_two_points),
                                        color = Color(0xFFA2A0A0),
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(0.5f))
                                Text(
                                    text = when (currentCategory) {
                                        PointCategory.GreenCard -> calculationUiState.greenCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        PointCategory.Coin -> calculationUiState.coinScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        else -> "" // esse caso em teoria nunca vai acontecer
                                    },
                                )
                                Spacer(modifier = Modifier.weight(0.5f))
                                TextButton(
                                    onClick = {
                                        when (currentCategory) {
                                            PointCategory.GreenCard -> onShowScienceGrid(nicknameList.indexOf(player))

                                            PointCategory.Coin -> onShowCoinGrid(
                                                nicknameList.indexOf(player),
                                                calculationUiState.coinQuantityList[nicknameList.indexOf(player)],
                                                calculationUiState.coinScoreList[nicknameList.indexOf(player)],
                                            )

                                            else -> {} // em teoria esse casso nunca vai acontecer
                                        }
                                    }
                                ) {
                                    Text(
                                        text = stringResource(R.string.generic_calculate),
                                        color = when (currentCategory) {
                                            PointCategory.GreenCard -> Color(0xFF63C963)
                                            PointCategory.Coin -> Color(0xFFDD8A10)
                                            else -> Color(0xFF000000) // em teoria não será usado
                                        } ,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Row {
            TextButton(
                onClick = onShowTotalGrid
            ) {
                Text(text = stringResource(R.string.calculation_show_total_grid))
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onPreviousCategory,
                enabled = (currentCategory.ordinal in 1..6)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(180f)
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
            IconButton(
                onClick = onNextCategory,
                enabled = (currentCategory.ordinal in 0..5)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun TotalScoringGrid(
    nicknameList: List<String>,
    calculationUiState: CalculationUiState,
    onShowParcialGrid: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spaceBetweenCards = 3.dp
    val cardsHeight = 56.dp
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .height(IntrinsicSize.Max)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spaceBetweenCards),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                for (player in nicknameList) {
                    NicknameCard(
                        nickname = player,
                        modifier = Modifier
                            .height(cardsHeight)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(spaceBetweenCards),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 0.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .height(40.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.general_socring_acronym).uppercase(),
                            modifier = Modifier
                                .padding(3.dp),
                            color = Color(0xFF000080)
                        )
                    }
                }
                for (player in nicknameList) {
                    Card(
                        shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .height(cardsHeight)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = calculationUiState.totalScoreList[nicknameList.indexOf(player)].toString(),
                                modifier = Modifier
                                    .padding(3.dp)
                                    .fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000080),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            for (i in 1..7) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spaceBetweenCards),
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                ) {
                    IconCategoryCard(
                        category = when (i) {
                            1 -> PointCategory.WonderBoard
                            2 -> PointCategory.Coin
                            3 -> PointCategory.War
                            4 -> PointCategory.BlueCard
                            5 -> PointCategory.YellowCard
                            6 -> PointCategory.GreenCard
                            else -> PointCategory.PurpleCard
                        },
                        lastCardInTheGrid = i == 7,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    for (player in nicknameList) {
                        Card(
                            shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .height(cardsHeight)
                                .fillMaxWidth(),
                            colors = when (i) {
                                1 -> CardDefaults.cardColors(Color(0xFFC4C4C4))
                                2 -> CardDefaults.cardColors(Color(0xFFF0D995))
                                3 -> CardDefaults.cardColors(Color(0xFFFFA7A7))
                                4 -> CardDefaults.cardColors(Color(0xFFB3BFFF))
                                5 -> CardDefaults.cardColors(Color(0xFFFFEB3B))
                                6 -> CardDefaults.cardColors(Color(0xFFB8FFBA))
                                else -> CardDefaults.cardColors(Color(0xFFDCC9FF))
                            },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = when (i) {
                                        1 -> calculationUiState.wonderBoardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        2 -> calculationUiState.coinScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        3 -> calculationUiState.warScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        4 -> calculationUiState.blueCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        5 -> calculationUiState.yellowCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        6 -> calculationUiState.greenCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()

                                        else -> calculationUiState.purpleCardScoreList[nicknameList.indexOf(
                                            player
                                        )].toString()
                                    },
                                    modifier = Modifier
                                        .padding(1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        Row {
            TextButton(
                onClick = onShowParcialGrid,
            ) {
                Text(text = stringResource(R.string.generic_back))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CoinGrid(
    playerShown: String,
    calculationUiState: CalculationUiState,
    onMinusOneCoinQuantity: (Int) -> Unit,
    onPlusOneCoinQuantity: (Int) -> Unit,
    onPlusTwoCoinsQuantity: (Int) -> Unit,
    onShowPartialGrid: () -> Unit,
    onCoinGridConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.calculation_screen_coin_grid_title),
            fontStyle = FontStyle.Italic,
            color = Color(0xFFDD8A10),
            modifier = Modifier
                .padding(3.dp)
        )
        Text(
            text = stringResource(R.string.calculation_screen_coin_grid_attention),
            fontStyle = FontStyle.Italic,
            color = Color(0xFFFF0000),
            modifier = Modifier
                .padding(3.dp),
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            NicknameCard(
                nickname = playerShown,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(max = 150.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { onMinusOneCoinQuantity(calculationUiState.playersList.indexOf(playerShown))  }
                        ) {
                            Text(
                                text = stringResource(R.string.minus_one_point),
                                color = Color(0xFFA2A0A0),
                                fontStyle = FontStyle.Italic
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = calculationUiState.coinQuantityList[calculationUiState.playersList.indexOf(playerShown)].toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDD8A10)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = { onPlusOneCoinQuantity(calculationUiState.playersList.indexOf(playerShown)) }
                        ) {
                            Text(
                                text = stringResource(R.string.plus_one_point),
                                color = Color(0xFFA2A0A0),
                                fontStyle = FontStyle.Italic
                            )
                        }
                        TextButton(
                            onClick = { onPlusTwoCoinsQuantity(calculationUiState.playersList.indexOf(playerShown)) }
                        ) {
                            Text(
                                text = stringResource(R.string.plus_two_points),
                                color = Color(0xFFA2A0A0),
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
        Row {
            TextButton(
                onClick = onShowPartialGrid,
            ) {
                Text(
                    text = stringResource(id = R.string.generic_back),
                    color = Color(0xFFDD8A10),
                    modifier = Modifier
                        .padding(end = 20.dp),
                )
            }
            TextButton(
                onClick = { onCoinGridConfirm(playerShown) }
            ) {
                Text(
                    text = stringResource(id = R.string.generic_confirm_text),
                    color = Color(0xFFDD8A10),
                    modifier = Modifier
                        .padding(start = 20.dp)

                )
            }
        }
    }
}

@Composable
fun ScienceGrid(
    playerShown: String,
    calculationUiState: CalculationUiState,
    onMinusOneScienceCard: (Int) -> Unit,
    onPlusOneScienceCard: (Int) -> Unit,
    onShowPartialGrid: () -> Unit,
    onScienceGridConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.calculation_science_grid_comand),
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1E9923),
            modifier = Modifier
                .padding(3.dp)
        )
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            NicknameCard(
                nickname = playerShown,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                ScienceIconCard(
                    scienceSymbol = ScienceSymbol.Compass,
                    modifier = Modifier.weight(1f)
                )
                ScienceIconCard(scienceSymbol = ScienceSymbol.Stone, modifier = Modifier.weight(1f))
                ScienceIconCard(scienceSymbol = ScienceSymbol.Gear, modifier = Modifier.weight(1f))
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                for (i in 0..2) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .widthIn(max = 150.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { onMinusOneScienceCard(i) }
                            ) {
                                Text(
                                    text = stringResource(R.string.minus_one_point),
                                    color = Color(0xFFA2A0A0),
                                    fontStyle = FontStyle.Italic
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = calculationUiState.scienceSymbolsCurrentQuantityList[i].toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E9923)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = { onPlusOneScienceCard(i) }
                            ) {
                                Text(
                                    text = stringResource(R.string.plus_one_point),
                                    color = Color(0xFFA2A0A0),
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                    }
                }
            }
        }
        Row {
            TextButton(
                onClick = onShowPartialGrid,
            ) {
                Text(
                    text = stringResource(id = R.string.generic_back),
                    color = Color(0xFF1E9923),
                    modifier = Modifier
                        .padding(end = 20.dp),
                )
            }
            TextButton(
                onClick = onScienceGridConfirm
            ) {
                Text(
                    text = stringResource(id = R.string.generic_confirm_text),
                    color = Color(0xFF1E9923),
                    modifier = Modifier
                        .padding(start = 20.dp)

                )
            }
        }
    }
}

@Composable
fun ScienceIconCard(
    scienceSymbol: ScienceSymbol,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Black),
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = when (scienceSymbol) {
                    ScienceSymbol.Compass -> painterResource(id = R.drawable.science_1)
                    ScienceSymbol.Stone -> painterResource(id = R.drawable.science_2)
                    ScienceSymbol.Gear -> painterResource(id = R.drawable.science_3)
                },
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun CoinGridPreview() {
    SevenWondersTheme {
        CoinGrid(
            playerShown = "Luiz",
            calculationUiState = CalculationUiState(),
            onMinusOneCoinQuantity = {},
            onPlusOneCoinQuantity = {},
            onShowPartialGrid = {},
            onCoinGridConfirm = {},
            onPlusTwoCoinsQuantity = {}
        )
    }
}

@Preview
@Composable
fun ScienceGridPreview() {
    SevenWondersTheme {
        ScienceGrid(
            playerShown = "Luiz",
            calculationUiState = CalculationUiState(),
            onMinusOneScienceCard = {},
            onPlusOneScienceCard = {},
            onShowPartialGrid = {},
            onScienceGridConfirm = {}
        )
    }
}

@Preview
@Composable
fun TotalScoringGridPreview() {
    SevenWondersTheme {
        TotalScoringGrid(
            nicknameList = listOf("Luiz", "Anna", "Deivinho", "Luighi"),
            calculationUiState = CalculationUiState(),
            onShowParcialGrid = {}
        )
    }
}

@Preview
@Composable
fun IconCategoryCardPreview() {
    SevenWondersTheme {
        IconCategoryCard(
            category = PointCategory.WonderBoard,
            lastCardInTheGrid = true
        )
    }
}


@Preview
@Composable
fun ScoringGridPreview() {
    SevenWondersTheme {
        ScoringGrid(
            nicknameList = listOf("Luiz", "Anna", "Deivinho", "Luighi"),
            currentCategory = PointCategory.WonderBoard,
            onPreviousCategory = { },
            onNextCategory = { },
            calculationUiState = CalculationUiState(),
            onPlusTwoPointsClick = {},
            onMinusOnePointClick = {},
            onShowTotalGrid = {},
            onShowScienceGrid = {},
            onPlusOnePointsClick = {},
            onShowCoinGrid = {_, _, _ ->}
        )
    }
}

@Preview
@Composable
fun NicknameCardPreview() {
    SevenWondersTheme {
        NicknameCard(
            nickname = "Zinho"
        )
    }
}

@Preview
@Composable
fun CategoryCardPreview() {
    SevenWondersTheme {
        CategoryCard(
            category = PointCategory.PurpleCard
        )
    }
}

@Preview
@Composable
fun CalculationScreenParcialGridSecundariaPreview() {
    SevenWondersTheme {
        CalculationScreenSecundaria(
            onBackClick = { /*TODO*/ },
            onNextClick = {},
            calculationUiState = CalculationUiState(
                playersList = listOf(
                    "Zinho",
                    "Anninha",
                    "Deivinho",
                )
            ),
            onPreviousCategory = { },
            onNextCategory = { },
            onPlusTwoPointsClick = {},
            onMinusOnePointClick = {},
            onShowPartialGrid = {},
            onShowTotalGrid = {},
            onShowScienceGrid = {},
            onPlusOneScienceCard = {},
            onMinusOneScienceCard = {},
            onScienceGridConfirm = {},
            onPlusOnePointsClick = {},
            onConfirmNextScreen = {},
            onDismissNextScreen = {},
            onCoinGridConfirm = {},
            onShowCoinGrid = {_, _, _ ->},
            onPlusOneCoinQuantity = {},
            onMinusOneCoinQuantity = {},
            onPlusTwoCoinsQuantity = {},
        )
    }
}

@Preview
@Composable
fun CalculationScreenTotalGridSecundariaPreview() {
    SevenWondersTheme {
        CalculationScreenSecundaria(
            onBackClick = { /*TODO*/ },
            onNextClick = {},
            calculationUiState = CalculationUiState(
                playersList = listOf(
                    "Zinho",
                    "Anninha",
                    "Deivinho",
                ),
                subScreen = CalculationSubScreen.TotalGrid,
            ),
            onPreviousCategory = { },
            onNextCategory = { },
            onPlusTwoPointsClick = {},
            onMinusOnePointClick = {},
            onShowPartialGrid = {},
            onShowTotalGrid = {},
            onShowScienceGrid = {},
            onPlusOneScienceCard = {},
            onMinusOneScienceCard = {},
            onScienceGridConfirm = {},
            onPlusOnePointsClick = {},
            onConfirmNextScreen = {},
            onDismissNextScreen = {},
            onCoinGridConfirm = {},
            onShowCoinGrid = {_, _, _ ->},
            onPlusOneCoinQuantity = {},
            onMinusOneCoinQuantity = {},
            onPlusTwoCoinsQuantity = {},
        )
    }
}

@Preview
@Composable
fun CalculationScreenScienceGridSecundariaPreview() {
    SevenWondersTheme {
        CalculationScreenSecundaria(
            onBackClick = { /*TODO*/ },
            onNextClick = {},
            calculationUiState = CalculationUiState(
                playersList = listOf(
                    "Zinho",
                    "Anninha",
                    "Deivinho",
                ),
                subScreen = CalculationSubScreen.ScienceGrid,
            ),
            onPreviousCategory = { },
            onNextCategory = { },
            onPlusTwoPointsClick = {},
            onMinusOnePointClick = {},
            onShowPartialGrid = {},
            onShowTotalGrid = {},
            onScienceGridConfirm = {},
            onMinusOneScienceCard = {},
            onPlusOneScienceCard = {},
            onShowScienceGrid = {},
            onPlusOnePointsClick = {},
            onConfirmNextScreen = {},
            onDismissNextScreen = {},
            onCoinGridConfirm = {},
            onShowCoinGrid = {_, _, _ ->},
            onPlusOneCoinQuantity = {},
            onMinusOneCoinQuantity = {},
            onPlusTwoCoinsQuantity = {},
        )
    }
}