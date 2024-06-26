package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.gmail.luizjmfilho.sevenwonders.SevenWondersNavHost
import com.gmail.luizjmfilho.sevenwonders.TestActivity
import com.gmail.luizjmfilho.sevenwonders.TestData.anna
import com.gmail.luizjmfilho.sevenwonders.TestData.cristian
import com.gmail.luizjmfilho.sevenwonders.TestData.luiz
import com.gmail.luizjmfilho.sevenwonders.ui.theme.SevenWondersTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SevenWondersIntegrationTest {

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<TestActivity>()
    private val sevenWondersRobot = SevenWondersRobot(rule)
    private val homeScreenRobot = HomeScreenRobot(rule)
    private val playersListScreenRobot = PlayersListScreenRobot(rule)
    private val newGameScreenRobot = NewGameScreenRobot(rule)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private fun launchScreen() {
        rule.setContent {
            SevenWondersTheme {
                SevenWondersNavHost(
                    windowWidthSizeClass = WindowWidthSizeClass.Compact
                )
            }
        }
    }

    @Before
    fun beforeTests() {
        hiltRule.inject() //essa linha ainda é necessária? Já que não tem nenhum @Inject.
    }

    @Test
    fun sevenWondersNavigation() {
        launchScreen()


        with(homeScreenRobot) {
            assertHomeScreenIsCurrentScreen()
            clickPlayersListButton()
        }
        playersListScreenRobot.assertPlayersListScreenIsCurrentScreen()
        sevenWondersRobot.clickNavigationBackButton()
        with(homeScreenRobot) {
            assertHomeScreenIsCurrentScreen()
            clickNewGameButton()
        }
        newGameScreenRobot.assertNewGameScreenIsCurrentScreen()
        sevenWondersRobot.clickNavigationBackButton()
        homeScreenRobot.assertHomeScreenIsCurrentScreen()
    }

    @Test
    fun whenPlayersListIsEmpty_ThenMessageShowsItInNewGameAvailablePlayersList() {
        launchScreen()

        homeScreenRobot.clickNewGameButton()
        with(newGameScreenRobot) {
            clickChoosePlayerButton(0)
            assertTextForEmptyAvailablePlayersListIsShown()
        }
    }

    @Test
    fun whenPlayersListHasPlayersRegistered_ThenTheyAreShownInNewGameAvailablePlayersList() {
        launchScreen()

        homeScreenRobot.clickPlayersListButton()
        with(playersListScreenRobot) {
            clickAddPlayerButton()
            typeNickname(luiz.name)
            clickConfirmAddPlayerButton()
            typeNickname(cristian.name)
            clickConfirmAddPlayerButton()
        }
        sevenWondersRobot.clickNavigationBackButton()
        homeScreenRobot.clickNewGameButton()
        with(newGameScreenRobot) {
            clickChoosePlayerButton(0)
            assertThereIsAPlayerInTheScreenWithThisNickname(luiz.name)
            assertThereIsAPlayerInTheScreenWithThisNickname(cristian.name)
            assertThereIsNoPlayerInTheScreenWithThisNickname(anna.name)
        }


    }

}