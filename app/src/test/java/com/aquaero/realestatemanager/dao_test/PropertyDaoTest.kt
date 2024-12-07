package com.aquaero.realestatemanager.dao_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.TypeEnum
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
/**
 * Testing PropertyDao
 */
class PropertyDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao

    private lateinit var propertyDao: PropertyDao

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var property3: Property
    private lateinit var properties: List<Property>
    private lateinit var property1updated: Property

    @Before
    fun setUp() {
        // Create the database in memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        // Prepopulate database
        agentDao = database.agentDao
        typeDao = database.typeDao
        poiDao = database.poiDao
        prepopulateDatabase()

        propertyDao = database.propertyDao

        property1 = Property(
            propertyId = 1L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = "Property1", surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = "2024-07-01", saleDate = null, agentId = 1L
        )
        property2 = Property(
            propertyId = 2L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = "Property2", surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = "2024-07-02", saleDate = "2024-07-31", agentId = 2L
        )
        property3 = Property(
            propertyId = 3L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = "Property3", surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = "2024-07-03", saleDate = null, agentId = 3L
        )
        property1updated = Property(
            propertyId = 1L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = "Property1Updated", surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = "2024-07-01", saleDate = null, agentId = 1L
        )

        properties = listOf(property2, property3, property1)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun prepopulateDatabase() = runBlocking {
        agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
        typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
        poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)
    }


    /**
     * Testing upsertProperty() and getProperty()
     */
    @Test
    fun testUpsertAndGetProperty() = runBlocking {
        // Function under test (inserting property)
        propertyDao.upsertProperty(property1)

        // Get the property from database to check the insertion (other function under test)
        var result = propertyDao.getProperty(1L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(property1.propertyId, result.propertyId)
        assertEquals(property1.description, result.description)

        // Function under test (updating property)
        propertyDao.upsertProperty(property1updated)

        // Get the property from database to check the update (other function under test)
        result = propertyDao.getProperty(1L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(property1.propertyId, result.propertyId)
        assertEquals(property1updated.propertyId, result.propertyId)
        assertNotEquals(property1.description, result.description)
        assertEquals(property1updated.description, result.description)
    }

    /**
     * Testing prepopulateWithProperties() and getProperties()
     */
    @Test
    fun testPrepopulateWithPropertiesAndGetProperties() = runBlocking {
        // Function under test
        var result = propertyDao.getProperties().first()

        // First assertion
        assertTrue(result.isEmpty())

        // Functions under test
        propertyDao.prepopulateWithProperties(properties)
        result = propertyDao.getProperties().first()

        // New assertion
        assertEquals(3, result.size)
    }

    @Test
    fun testGetPropertiesOrderedById() = runBlocking {
        // Prepopulate database
        propertyDao.prepopulateWithProperties(properties)

        // Functions under test
        val result = propertyDao.getPropertiesOrderedById().first()

        // Assertions
        assertEquals(3, result.size)
        assertEquals(1L, result[0].propertyId)
        assertEquals(2L, result[1].propertyId)
        assertEquals(3L, result[2].propertyId)
    }

    @Test
    fun testGetPropertiesOrderedByRegistrationDate() = runBlocking {
        // Prepopulate database
        propertyDao.prepopulateWithProperties(properties)

        // Functions under test
        val result = propertyDao.getPropertiesOrderedByRegistrationDate().first()

        // Assertions (properties should be sorted by descending registration dates)
        assertEquals(3, result.size)
        assertEquals(3L, result[0].propertyId)
        assertEquals("2024-07-03", result[0].registrationDate)
        assertEquals(2L, result[1].propertyId)
        assertEquals("2024-07-02", result[1].registrationDate)
        assertEquals(1L, result[2].propertyId)
        assertEquals("2024-07-01", result[2].registrationDate)
    }


    // ContentProvider

    @Test
    fun testGetPropertiesWithCursor() = runBlocking {
        // Prepopulate database
        propertyDao.prepopulateWithProperties(properties)

        // Function under test
        val cursor = propertyDao.getPropertiesWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(1L, cursor.getLong(0))
        assertEquals("Property1", cursor.getString(4))
        assertEquals(null, cursor.getString(10))
        cursor.moveToPosition(1)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("Property2", cursor.getString(4))
        assertEquals("2024-07-31", cursor.getString(10))
        cursor.moveToPosition(2)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("Property3", cursor.getString(4))
        assertEquals(null, cursor.getString(10))

        // Close cursor
        cursor.close()
    }

    @Test
    fun testGetPropertiesForSaleWithCursor() = runBlocking {
        // Prepopulate database
        propertyDao.prepopulateWithProperties(properties)

        // Function under test
        val cursor = propertyDao.getPropertiesForSaleWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(2, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(1L, cursor.getLong(0))
        assertEquals("Property1", cursor.getString(4))
        assertEquals(null, cursor.getString(10))
        cursor.moveToPosition(1)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("Property3", cursor.getString(4))
        assertEquals(null, cursor.getString(10))

        // Close cursor
        cursor.close()
    }

    @Test
    fun testGetSoldPropertiesWithCursor() = runBlocking {
        // Prepopulate database
        propertyDao.prepopulateWithProperties(properties)

        // Function under test
        val cursor = propertyDao.getSoldPropertiesWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(1, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("Property2", cursor.getString(4))
        assertEquals("2024-07-31", cursor.getString(10))

        // Close cursor
        cursor.close()
    }

}