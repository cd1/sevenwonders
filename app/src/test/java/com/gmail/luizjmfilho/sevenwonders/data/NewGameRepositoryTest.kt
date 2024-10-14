package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.TestData.anna
import com.gmail.luizjmfilho.sevenwonders.TestData.cristian
import com.gmail.luizjmfilho.sevenwonders.TestData.luiz
import com.gmail.luizjmfilho.sevenwonders.model.Player
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class NewGameRepositoryTest {

    private lateinit var dao: PlayerDao
    private lateinit var repository: NewGameRepository

    @Before
    fun beforeTests() {
        dao = mock()
        repository = NewGameRepository(dao)
    }

    private suspend fun mockReadPlayerExcept(excludedNicknames: List<String>, result: List<Player>) {
        whenever(dao.selectPeopleExcept(excludedNicknames))
            .thenReturn(result)
    }

    @Test
    fun onReadPlayerWithoutActivePlayers_ThenItForwardsTheParameter() = runTest {
        mockReadPlayerExcept(listOf(cristian.name), listOf(luiz, anna))

        val listReturned = repository.readPlayerWithoutActivePlayers(listOf(cristian.name))

        assertEquals(listOf(luiz, anna), listReturned)
    }

}