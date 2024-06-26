package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.gmail.luizjmfilho.sevenwonders.TestData.anna
import com.gmail.luizjmfilho.sevenwonders.TestData.cristian
import com.gmail.luizjmfilho.sevenwonders.TestData.gian
import com.gmail.luizjmfilho.sevenwonders.TestData.ivana
import com.gmail.luizjmfilho.sevenwonders.TestData.luiz
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NewGameScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()
    private val robot = NewGameScreenRobot(rule)
    private val stateRestorationTester = StateRestorationTester(rule) // essa é uma rule tbm, mas que permite testar as configuration chances.

    private fun launchScreen(
        onBackClick: () -> Unit = {},
        onAdvanceClick: (List<String>) -> Unit = {},
        onPlayerChange: (Int, String) -> Unit = {_, _ ->},
        onChoosePlayerClick: () -> Unit = {},
        onAddPlayerTextButtonClick: () -> Unit = {},
        onRemovePlayerTextButtonClick: () -> Unit = {},
        newGameUiState: NewGameUiState = NewGameUiState(),
    ) {
        stateRestorationTester.setContent{
            NewGameScreenSecundaria(
                onBackClick = onBackClick,
                onNextClick = onAdvanceClick,
                onPlayerChange = onPlayerChange,
                onChoosePlayerClick = onChoosePlayerClick,
                onAddPlayerTextButtonClick = onAddPlayerTextButtonClick,
                onRemovePlayerTextButtonClick = onRemovePlayerTextButtonClick,
                uiState = newGameUiState,
            )
        }

    }


    @Test
    fun initialState() {
        launchScreen()

        with(robot) {
            assertAddButtonIsDisabled()
            assertAdvanceButtonIsDisabled()
            assertRemoveButtonIsNotShown()
            assertTextFieldInPositionIsEmpty(0)
            assertTextFieldInPositionIsEmpty(1)
            assertTextFieldInPositionIsEmpty(2)
            assertThereIsNoTextFieldPlayerThatPositionIs(3)
        }
    }

    @Test
    fun onBackButtonClick() {
        val sevenWondersRobot = SevenWondersRobot(rule)
        var backButtonClicked = false
        launchScreen(onBackClick = { backButtonClicked = true })

        sevenWondersRobot.clickNavigationBackButton()
        assertTrue(backButtonClicked)
    }

    @Test
    fun onAdvanceButtonClick() {
        val testList = listOf(luiz.name, cristian.name, anna.name)
        var activePlayersList: List<String>? = null
        launchScreen(
            onAdvanceClick = { listaDeNicknames ->
                activePlayersList = listaDeNicknames
            },
            newGameUiState = NewGameUiState(
                isAdvanceAndAddPlayerButtonsEnable = true,
                playerNames = testList
            ),
        )

        robot.clickAdvanceButton()

        assertEquals(testList, activePlayersList)
    }

    @Test
    fun onChoosePlayerClick() {
        var choosePlayerButtonWasClicked = false
        launchScreen(
            onChoosePlayerClick = { choosePlayerButtonWasClicked = true },
        )

        robot.clickChoosePlayerButton(1)

        assertTrue(choosePlayerButtonWasClicked)
    }

    @Test
    fun onPlayerChange() {
        var expectedNickname: String? = null
        var expectedPosition: Int? = null
        launchScreen(
            onPlayerChange = { position, nickname ->
                expectedPosition = position
                expectedNickname = nickname
            },
            newGameUiState = NewGameUiState(
                availablePlayersList = listOf(anna, luiz, cristian)
            )
        )

        with(robot) {
            clickChoosePlayerButton(1)
            clickOnAvailablePlayersListInPlayerThatNicknameIs(luiz.name)
            clickGenericConfirmButton()
        }

        assertEquals(luiz.name, expectedNickname)
        assertEquals(1, expectedPosition)


    }

    @Test
    fun onAddPlayerTextButtonClick() {
        var addPlayerTextButtonWasClicked = false
        launchScreen(
            onAddPlayerTextButtonClick = { addPlayerTextButtonWasClicked = true },
            newGameUiState = NewGameUiState(
                isAdvanceAndAddPlayerButtonsEnable = true
            )
        )

        robot.clickAddPlayerButton()

        assertTrue(addPlayerTextButtonWasClicked)
    }

    @Test
    fun onRemovePlayerTextButtonClick() {
        var removePlayerTextButtonWasClicked = false
        launchScreen(
            onRemovePlayerTextButtonClick = { removePlayerTextButtonWasClicked = true },
            newGameUiState = NewGameUiState(
                activePlayersNumber = ActivePlayersNumber.Four
            )
        )

        robot.clickRemovePlayerButton()

        assertTrue(removePlayerTextButtonWasClicked)
    }

    @Test
    fun whenActivePlayersListHasStrings_ThenRelatedTextFieldsShowIt() {
        launchScreen(
            newGameUiState = NewGameUiState(
                playerNames = listOf(luiz.name, anna.name, "", "", "", "", "")
            )
        )

        with(robot) {
            assertTextFieldInThePositionHasNickname(0, luiz.name)
            assertTextFieldInThePositionHasNickname(1, anna.name)
            assertTextFieldInThePositionHasNickname(2, "")
        }
    }

    @Test
    fun when5ActivePlayers_Then5TextFieldsAreShown() {
        launchScreen(
            newGameUiState = NewGameUiState(
                activePlayersNumber = ActivePlayersNumber.Five
            )
        )

        with(robot) {
            assertThereIsTextFieldPlayerThatPositionIs(4)
            assertThereIsNoTextFieldPlayerThatPositionIs(5)
        }
    }

    @Test
    fun when7ActivePlayers_Then7TextFieldsAreShown() {
        launchScreen(
            newGameUiState = NewGameUiState(
                activePlayersNumber = ActivePlayersNumber.Seven
            )
        )

        robot.assertThereIsTextFieldPlayerThatPositionIs(6)
    }

    @Test
    fun when7ActivePlayers_ThenAddPlayerTextButtonIsNotShown() {
        launchScreen(
            newGameUiState = NewGameUiState(
                activePlayersNumber = ActivePlayersNumber.Seven
            )
        )

        robot.assertAddPlayerButtonIsNotShown()
    }

    @Test
    fun whenActivePlayersNumberIsNot3_ThenRemovePlayerTextButtonIsShown() {
        launchScreen(
            newGameUiState = NewGameUiState(
                activePlayersNumber = ActivePlayersNumber.Four
            )
        )

        robot.assertRemoveButtonIsShown()
    }

    @Test
    fun whenAvailablePlayersListHasNames_ThenTheyAreShowInPlayersListDialog() {
        launchScreen(
            newGameUiState = NewGameUiState(
                availablePlayersList = listOf(luiz, anna, cristian, ivana)
            )
        )

        robot.clickChoosePlayerButton(0)

        with(robot) {
            assertThereIsAPlayerInTheScreenWithThisNickname(luiz.name)
            assertThereIsAPlayerInTheScreenWithThisNickname(anna.name)
            assertThereIsAPlayerInTheScreenWithThisNickname(cristian.name)
            assertThereIsAPlayerInTheScreenWithThisNickname(ivana.name)
            assertThereIsNoPlayerInTheScreenWithThisNickname(gian.name)
        }

    }

    @Test
    fun whenIsAdvanceAndAddPlayerButtonsEnableIsFalse_ThenBothButtonsAreNotClickable() {
        var addPlayerButtonWasClicked = false
        var advanceButtonWasClicked = false
        launchScreen(
            onAdvanceClick = { advanceButtonWasClicked = true } ,
            onAddPlayerTextButtonClick = { addPlayerButtonWasClicked = true } ,
            newGameUiState = NewGameUiState(
                isAdvanceAndAddPlayerButtonsEnable = false
            )
        )

        with(robot) {
            clickAddPlayerButton()
            clickAdvanceButton()
        }

        assertFalse(addPlayerButtonWasClicked)
        assertFalse(advanceButtonWasClicked)
    }

    @Test
    fun whenAdvanceAndAddPlayerButtonsEnableIsTrue_ThenBothButtonsAreClickable() {
        var addPlayerButtonWasClicked = false
        var advanceButtonWasClicked = false
        launchScreen(
            onAdvanceClick = { advanceButtonWasClicked = true } ,
            onAddPlayerTextButtonClick = { addPlayerButtonWasClicked = true } ,
            newGameUiState = NewGameUiState(
                isAdvanceAndAddPlayerButtonsEnable = true
            )
        )

        with(robot) {
            clickAddPlayerButton()
            clickAdvanceButton()
        }

        assertTrue(addPlayerButtonWasClicked)
        assertTrue(advanceButtonWasClicked)
    }

    @Test
    fun whenConfigurationChange_ThenPlayerStillSelectedInAvailablePlayersList() {
        launchScreen(
            newGameUiState = NewGameUiState(
                availablePlayersList = listOf(luiz, anna, cristian)
            )
        )

        with(robot) {
            clickChoosePlayerButton(0)
            clickOnAvailablePlayersListInPlayerThatNicknameIs(luiz.name)
        }
        stateRestorationTester.emulateSavedInstanceStateRestore()

        robot.assertPlayerIsSelected(luiz.name)
    }



}