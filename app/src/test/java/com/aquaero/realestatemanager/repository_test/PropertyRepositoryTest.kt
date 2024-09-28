package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import kotlin.properties.Delegates

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing PropertyRepository
 */
class PropertyRepositoryTest {
    private lateinit var propertyDao: PropertyDao
    private lateinit var repository: PropertyRepository

    private var propertyId1 by Delegates.notNull<Long>()
    private var propertyId2 by Delegates.notNull<Long>()
    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var properties: MutableList<Property>
    private lateinit var propertiesOrderedById: MutableList<Property>
    private lateinit var property1Flow: Flow<Property>
    private lateinit var propertiesFlow: Flow<MutableList<Property>>
    private lateinit var propertiesOrderedByIdFlow: Flow<MutableList<Property>>

    @Before
    fun setup() {
        propertyDao = mock(PropertyDao::class.java)
        property1 = mock(Property::class.java)
        property2 = mock(Property::class.java)

        repository = PropertyRepository(propertyDao)

        propertyId1 = 1L
        propertyId2 = 2L
        properties = mutableListOf(property2, property1)
        propertiesOrderedById = mutableListOf(property1, property2)
        property1Flow = flowOf(property1)
        propertiesFlow = flowOf(properties)
        propertiesOrderedByIdFlow = flowOf(propertiesOrderedById)

        // Setup mocks behaviour
        runBlocking { doReturn(propertyId1).`when`(propertyDao).upsertProperty(property1) }
        doReturn(propertyId2).`when`(property2).propertyId
        doReturn(property1Flow).`when`(propertyDao).getProperty(propertyId1)
        doReturn(propertiesFlow).`when`(propertyDao).getProperties()
        doReturn(propertiesOrderedByIdFlow).`when`(propertyDao).getPropertiesOrderedById()
        doReturn(propertiesFlow).`when`(propertyDao).getPropertiesOrderedByRegistrationDate()
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertPropertyInRoom() = runBlocking {
        // Function under test
        val result = repository.upsertPropertyInRoom(property1)

        // Verification and assertion
        verify(propertyDao).upsertProperty(property1)
        assertEquals(propertyId1, result)
    }

    @Test
    fun testGetPropertyFromRoom() {
        // Function under test
        val result = repository.getPropertyFromRoom(propertyId1)

        // Verification and assertion
        verify(propertyDao).getProperty(propertyId1)
        assertEquals(property1Flow, result)
    }

    @Test
    fun testGetPropertiesFromRoom() {
        // Function under test
        val result = repository.getPropertiesFromRoom()

        // Verification and assertion
        verify(propertyDao).getProperties()
        assertEquals(propertiesFlow, result)
    }

    @Test
    fun testGetPropertiesOrderedByIdFromRoom() {
        // Function under test
        val result = repository.getPropertiesOrderedByIdFromRoom()

        // Verification and assertion
        verify(propertyDao).getPropertiesOrderedById()
        assertEquals(propertiesOrderedByIdFlow, result)
    }

    @Test
    fun testGetPropertiesOrderedByRegistrationDateFromRoom() {
        // Function under test
        val result = repository.getPropertiesOrderedByRegistrationDateFromRoom()

        // Verification and assertion
        verify(propertyDao).getPropertiesOrderedByRegistrationDate()
        assertEquals(propertiesFlow, result)
    }

    @Test
    fun testPropertyFromId() {
        // Function under test
        val result = repository.propertyFromId(propertyId2, properties)
        // Assertion
        assertEquals(property2, result)
    }

}