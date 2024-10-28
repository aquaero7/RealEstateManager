package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
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
import kotlin.properties.Delegates

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing PropertyPoiJoinRepository
 */
class PropertyPoiJoinRepositoryTest {
    private lateinit var propertyPoiJoinDao: PropertyPoiJoinDao
    private lateinit var repository: PropertyPoiJoinRepository

    private lateinit var propertyPoiJoin1: PropertyPoiJoin
    private lateinit var propertyPoiJoin2: PropertyPoiJoin
    private lateinit var propertyPoiJoins: MutableList<PropertyPoiJoin>
    private lateinit var propertyPoiJoinsFlow: Flow<MutableList<PropertyPoiJoin>>
    private var propertyId1 by Delegates.notNull<Long>()
    private lateinit var poiId1: String
    private lateinit var poi1: Poi
    private lateinit var poi2: Poi
    private lateinit var pois: MutableList<Poi>
    private lateinit var poisFlow: Flow<MutableList<Poi>>
    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var properties: MutableList<Property>
    private lateinit var propertiesFlow: Flow<MutableList<Property>>

    @Before
    fun setup() {
        propertyPoiJoinDao = mock(PropertyPoiJoinDao::class.java)
        repository = PropertyPoiJoinRepository(propertyPoiJoinDao)

        propertyPoiJoin1 = mock(PropertyPoiJoin::class.java)
        propertyPoiJoin2 = mock(PropertyPoiJoin::class.java)
        propertyPoiJoins = mutableListOf(propertyPoiJoin2, propertyPoiJoin1)
        propertyPoiJoinsFlow = flowOf(propertyPoiJoins)
        propertyId1 = 1L
        poi1 = mock(Poi::class.java)
        poi2 = mock(Poi::class.java)
        pois = mutableListOf(poi2, poi1)
        poisFlow = flowOf(pois)
        poiId1 = "poiId1"
        property1 = mock(Property::class.java)
        property2 = mock(Property::class.java)
        properties = mutableListOf(property2, property1)
        propertiesFlow = flowOf(properties)

        // Setup mocks behaviour
        doReturn(poisFlow).`when`(propertyPoiJoinDao).getPoisForProperty(propertyId1)
        doReturn(propertiesFlow).`when`(propertyPoiJoinDao).getPropertiesForPoi(poiId1)
        doReturn(propertyPoiJoinsFlow).`when`(propertyPoiJoinDao).getPropertyPoiJoins()
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertPropertyPoiJoinInRoom() = runBlocking {
        // Function under test
        repository.upsertPropertyPoiJoinInRoom(propertyPoiJoin1)

        // Verification
        verify(propertyPoiJoinDao).upsertPropertyPoiJoin(propertyPoiJoin1)
    }

    @Test
    fun testDeletePropertyPoiJoinFromRoom() = runBlocking {
        // Function under test
        repository.deletePropertyPoiJoinFromRoom(propertyPoiJoin1)

        // Verification
        verify(propertyPoiJoinDao).deletePropertyPoiJoin(propertyPoiJoin1)
    }

    @Test
    fun testUpsertPropertyPoiJoinsInRoom() = runBlocking {
        // Function under test
        repository.upsertPropertyPoiJoinsInRoom(propertyPoiJoins)

        // Verification
        verify(propertyPoiJoinDao).upsertPropertyPoiJoins(propertyPoiJoins)
    }

    @Test
    fun testDeletePropertyPoiJoinsFromRoom() = runBlocking {
        // Function under test
        repository.deletePropertyPoiJoinsFromRoom(propertyPoiJoins)

        // Verification
        verify(propertyPoiJoinDao).deletePropertyPoiJoins(propertyPoiJoins)
    }

    @Test
    fun testGetPoisForPropertyFromRoom() {
        // Function under test
        val result = repository.getPoisForPropertyFromRoom(propertyId1)

        // Verification and assertion
        verify(propertyPoiJoinDao).getPoisForProperty(propertyId1)
        Assert.assertEquals(poisFlow, result)
    }

    @Test
    fun testGetPropertiesForPoiFromRoom() {
        // Function under test
        val result = repository.getPropertiesForPoiFromRoom(poiId1)

        // Verification and assertion
        verify(propertyPoiJoinDao).getPropertiesForPoi(poiId1)
        Assert.assertEquals(propertiesFlow, result)
    }

    @Test
    fun testGetPropertyPoiJoinsFromRoom() {
        // Function under test
        val result = repository.getPropertyPoiJoinsFromRoom()

        // Verification and assertion
        verify(propertyPoiJoinDao).getPropertyPoiJoins()
        Assert.assertEquals(propertyPoiJoinsFlow, result)
    }

}