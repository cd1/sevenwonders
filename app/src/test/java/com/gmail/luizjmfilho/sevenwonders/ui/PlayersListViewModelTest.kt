package com.gmail.luizjmfilho.sevenwonders.ui

import com.gmail.luizjmfilho.sevenwonders.MainDispatcherRule
import com.gmail.luizjmfilho.sevenwonders.data.AddPlayerResult
import com.gmail.luizjmfilho.sevenwonders.data.PlayersListRepository
import com.gmail.luizjmfilho.sevenwonders.model.Person
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class PlayersListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PlayersListRepository
    private lateinit var viewModel: PlayersListViewModel

    @Before
    fun beforeTests() = runTest {
        repository = mock()
        mockReadPlayer()
        viewModel = PlayersListViewModel(repository)
    }

    private suspend fun mockAddPlayer(nickname: String = "", result: AddPlayerResult? = null) {
        whenever(repository.addPlayer(nickname))
            .thenReturn(result)
    }

    private suspend fun mockReadPlayer(list: List<Person> = emptyList()) {
        whenever(repository.readPlayer())
            .thenReturn(list)
    }

    private suspend fun verifyAddPlayer(times: Int = 1, nickname: String = "") {
        verify(repository, times(times)).addPlayer(nickname)
    }

    @Test
    fun initialState() = runTest {
        val initialState = viewModel.uiState.value
        assertEquals("", initialState.nickname)
        assertEquals(listOf<Person>(), initialState.playerNames)
        assertNull(initialState.nicknameError)
    }

    @Test
    fun onConfirmAddPlayerClick_whenNameAndNicknameEmpty_thenIsErrorEmpty() = runTest {
        mockAddPlayer(result = AddPlayerResult(NameOrNicknameError.Empty))

        viewModel.onConfirmAddPlayerClick()

        verifyAddPlayer()
        val state = viewModel.uiState.value
        assertEquals(NameOrNicknameError.Empty ,state.nicknameError)
        assertEquals(listOf<Person>(), state.playerNames)
    }

    @Test
    fun updateNickname_whenITypeSomething() = runTest {
        viewModel.updateNickname("Zinho")
        val state = viewModel.uiState.value

        assertEquals("Zinho", state.nickname)
    }

    @Test
    fun cancelAddPlayer_whenIClickOnIt() = runTest {
        viewModel.updateNickname("Zinho")
        viewModel.cancelAddPlayer()
        val state = viewModel.uiState.value

        assertEquals("", state.nickname)
    }

    @Test
    fun deletePlayer_whenIClickOnIt() = runTest {
        mockReadPlayer(
            listOf(
                Person("Oi")
            )
        )
        viewModel.onDeletePlayer("Luiz")

        val state = viewModel.uiState.value

        verify(repository, times(1)).deletePlayer("Luiz")
        verify(repository, times(2)).readPlayer()
        assertEquals(listOf(Person("Oi")), state.playerNames)
    }

    @Test
    fun onConfirmAddPlayerClick_WhenHappyPath() = runTest {
        whenever(repository.readPlayer())
            .thenReturn(listOf(Person("Luiz")))

        viewModel.updateNickname("Zinho")
        viewModel.onConfirmAddPlayerClick()
        val state = viewModel.uiState.value

        assertEquals("" ,state.nickname)
        assertEquals(listOf(Person("Luiz")) ,state.playerNames)
        assertNull(state.nicknameError)
    }
}