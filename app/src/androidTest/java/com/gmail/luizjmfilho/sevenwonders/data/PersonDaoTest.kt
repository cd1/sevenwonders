package com.gmail.luizjmfilho.sevenwonders.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.gmail.luizjmfilho.sevenwonders.TestData.anna
import com.gmail.luizjmfilho.sevenwonders.TestData.cristian
import com.gmail.luizjmfilho.sevenwonders.TestData.luiz
import com.gmail.luizjmfilho.sevenwonders.model.Player
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PersonDaoTest {

    private lateinit var dao: PlayerDao

    @Before
    fun beforeTests() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(context, SevenWondersDatabase::class.java).build()
        dao = database.playerDao()
    }

    @Test
    fun onAddPlayer_WhenInsertAPerson_HappyPath() = runTest {
        dao.insert(luiz)

        val currentList = dao.selectAll()

        assertTrue(listOf(luiz) == currentList)
    }

    @Test
    fun onDeletePlayer_WhenDeleteAPerson_HappyPath() = runTest {
        dao.insert(luiz)
        dao.delete(luiz.name)

        val currentList = dao.selectAll()

        assertEquals(emptyList<Player>(), currentList)
    }

    @Test
    fun onReadPlayer_WhenAddTwoPlayersOrMore_ThenListIsAlphabeticallyOrdered() = runTest {
        dao.insert(luiz)
        dao.insert(anna)
        dao.insert(cristian)

        val currentList = dao.selectAll()
        assertTrue(listOf(anna, cristian, luiz) == currentList)
    }

    @Test
    fun onNumberOfPlayersWithThisNickname_WhenNicknameHaveDifferentCases_ThenItCountsJustOne() = runTest {
        dao.insert(luiz)
        val numberOfPlayers = dao.selectNameAlreadyExists(luiz.name.uppercase())

        assertEquals(1, numberOfPlayers)
    }

    @Test
    fun onNumberOfPlayersWithThisNickname_WhenNoPlayers_ThenReturnZero() = runTest {
        val numberOfPlayers = dao.selectNameAlreadyExists(luiz.name)
        assertEquals(0, numberOfPlayers)
    }

    @Test
    fun onNumberOfPlayersWithThisNickname_WhenOnePlayerWithThatNickname_ThenReturnsOne() = runTest {
        dao.insert(luiz)
        dao.insert(cristian)
        val numberOfPlayers = dao.selectNameAlreadyExists(luiz.name)
        assertEquals(1, numberOfPlayers)
    }

    @Test
    fun onNumberOfPlayersWithThisNickname_WhenTwoPlayersWithThatNickname_ThenReturnsTwo() = runTest {
        dao.insert(anna)
        dao.insert(anna.copy(id = 5))
        dao.insert(cristian)
        val numberOfPlayers = dao.selectNameAlreadyExists(anna.name)
        assertEquals(2, numberOfPlayers)
    }

    @Test
    fun onReadPlayerExcept_WhenFilteringJustOnePerson_ThenReturnsFilteredAndOrderedList() = runTest {
        dao.insert(cristian)
        dao.insert(anna)
        dao.insert(luiz)

        val filteredList = dao.selectPeopleExcept(listOf(luiz.name))

        assertTrue(listOf(anna, cristian) == filteredList)
    }

    @Test
    fun onReadPlayerExcept_WhenFilteringNoOne_ThenReturnsFilteredAndOrderedList() = runTest {
        dao.insert(cristian)
        dao.insert(anna)
        dao.insert(luiz)

        val filteredList = dao.selectPeopleExcept(emptyList())

        assertTrue(listOf(anna, cristian, luiz) == filteredList)
    }

    @Test
    fun onReadPlayerExcept_WhenFilteringEveryone_ThenReturnsEmptyList() = runTest {
        dao.insert(cristian)
        dao.insert(anna)
        dao.insert(luiz)

        val filteredList = dao.selectPeopleExcept(listOf(luiz.name, anna.name, cristian.name))

        assertTrue(filteredList.isEmpty())
    }
}