package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.repository.PoiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing PoiRepository
 */
class PoiRepositoryTest {
    private lateinit var poiDao: PoiDao
    private lateinit var repository: PoiRepository

    private lateinit var poiId1: String
    private lateinit var poiId2: String
    private lateinit var poi1: Poi
    private lateinit var poi2: Poi
    private lateinit var poi1Flow: Flow<Poi>
    private lateinit var pois: MutableList<Poi>
    private lateinit var poisOrderedById: MutableList<Poi>
    private lateinit var poisFlow: Flow<MutableList<Poi>>
    private lateinit var poisOrderedByIdFlow: Flow<MutableList<Poi>>

    @Before
    fun setup() {
        poiDao = mock(PoiDao::class.java)
        repository = PoiRepository(poiDao)

        poiId1 = "poiId1"
        poiId2 = "poiId2"
        poi1 = mock(Poi::class.java)
        poi2 = mock(Poi::class.java)
        poi1Flow = flowOf(poi1)
        pois = mutableListOf(poi2, poi1)
        poisOrderedById = mutableListOf(poi1, poi2)
        poisFlow = flowOf(pois)
        poisOrderedByIdFlow = flowOf(poisOrderedById)

        // Setup mocks behaviour
        doReturn(poi1Flow).`when`(poiDao).getPoi(poiId1)
        doReturn(poisFlow).`when`(poiDao).getPois()
        doReturn(poisOrderedByIdFlow).`when`(poiDao).getPoisOrderedById()
        doReturn(poiId2).`when`(poi2).poiId
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertPoiInRoom() = runBlocking {
        // Function under test
        repository.upsertPoiInRoom(poi1)

        // Verification
        verify(poiDao).upsertPoi(poi1)
    }

    @Test
    fun testDeletePoiFromRoom() = runBlocking {
        // Function under test
        repository.deletePoiFromRoom(poi1)

        // Verification
        verify(poiDao).deletePoi(poi1)
    }

    @Test
    fun testGetPoiFromRoom() {
        // Function under test
        val result = repository.getPoiFromRoom(poiId1)

        // Verification and assertion
        verify(poiDao).getPoi(poiId1)
        Assert.assertEquals(poi1Flow, result)
    }

    @Test
    fun testGetPoisFromRoom() {
        // Function under test
        val result = repository.getPoisFromRoom()

        // Verification and assertion
        verify(poiDao).getPois()
        Assert.assertEquals(poisFlow, result)
    }

    @Test
    fun testGetPoisOrderedByIdFromRoom() {
        // Function under test
        val result = repository.getPoisOrderedByIdFromRoom()

        // Verification and assertion
        verify(poiDao).getPoisOrderedById()
        Assert.assertEquals(poisOrderedByIdFlow, result)
    }

    @Test
    fun testPoiFromId() {
        // Function under test
        val result = repository.poiFromId(poiId2, pois)

        // Verification and assertion
        Assert.assertEquals(poi2, result)
    }

}