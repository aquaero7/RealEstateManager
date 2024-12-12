package com.aquaero.realestatemanager.dao_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.TypeEnum
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
/**
 * Testing PropertyPoiJoinDao
 */
class PropertyPoiJoinDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao
    private lateinit var propertyDao: PropertyDao

    private lateinit var propertyPoiJoinDao: PropertyPoiJoinDao

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var property3: Property
    private lateinit var poi1: Poi
    private lateinit var poi2: Poi
    private lateinit var poi3: Poi

    private lateinit var propertyPoiJoin11: PropertyPoiJoin
    private lateinit var propertyPoiJoin12: PropertyPoiJoin
    private lateinit var propertyPoiJoin22: PropertyPoiJoin
    private lateinit var propertyPoiJoin23: PropertyPoiJoin
    private lateinit var propertyPoiJoin31: PropertyPoiJoin
    private lateinit var propertyPoiJoin33: PropertyPoiJoin
    private lateinit var propertyPoiJoins: List<PropertyPoiJoin>
    private lateinit var propertyPoiJoinsToDelete: MutableList<PropertyPoiJoin>

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
        propertyDao = database.propertyDao
        prepopulateDatabase()

        propertyPoiJoinDao = database.propertyPoiJoinDao

        propertyPoiJoin11 = PropertyPoiJoin(1L, "Poi1")
        propertyPoiJoin12 = PropertyPoiJoin(1L, "Poi2")
        propertyPoiJoin22 = PropertyPoiJoin(2L, "Poi2")
        propertyPoiJoin23 = PropertyPoiJoin(2L, "Poi3")
        propertyPoiJoin31 = PropertyPoiJoin(3L, "Poi1")
        propertyPoiJoin33 = PropertyPoiJoin(3L, "Poi3")

        propertyPoiJoins = listOf(
            propertyPoiJoin11, propertyPoiJoin12, propertyPoiJoin22, propertyPoiJoin23,
            propertyPoiJoin31, propertyPoiJoin33
        )
        propertyPoiJoinsToDelete =
            mutableListOf(propertyPoiJoin12, propertyPoiJoin23, propertyPoiJoin31)
    }

    private fun prepopulateDatabase() = runBlocking {
        agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
        typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
        poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)

        property1 = Property(
            propertyId = 1L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = 1L
        )
        property2 = Property(
            propertyId = 2L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = 2L
        )
        property3 = Property(
            propertyId = 3L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = 3L
        )
        propertyDao.prepopulateWithProperties(listOf(property1, property2, property3))

        poi1 = Poi("Poi1")
        poi2 = Poi("Poi2")
        poi3 = Poi("Poi3")
        poiDao.prepopulateWithPois(listOf(poi1, poi2, poi3))
    }


    /**
     * Testing upsertPropertyPoiJoin(), getPoisForProperty and getPropertiesForPoi()
     */
    @Test
    fun testUpsertPropertyPoiJoinAndGetPoisAndProperties() = runBlocking {
        // Function under test (inserting propertyPoiJoin)
        propertyPoiJoinDao.upsertPropertyPoiJoin(propertyPoiJoin11)
        propertyPoiJoinDao.upsertPropertyPoiJoin(propertyPoiJoin12)
        propertyPoiJoinDao.upsertPropertyPoiJoin(propertyPoiJoin22)

        // Get the pois for properties from database to check the insertion (other function under test)
        val poisResult1 = propertyPoiJoinDao.getPoisForProperty(1L).first()
        val poisResult2 = propertyPoiJoinDao.getPoisForProperty(2L).first()

        // Assertions
        assertNotNull(poisResult1)
        assertNotNull(poisResult2)
        assertEquals(mutableListOf(poi1, poi2), poisResult1)
        assertEquals(mutableListOf(poi2), poisResult2)

        // Get the properties for pois from database to check the insertion (other function under test)
        val propertiesResult1 = propertyPoiJoinDao.getPropertiesForPoi("Poi1").first()
        val propertiesResult2 = propertyPoiJoinDao.getPropertiesForPoi("Poi2").first()

        // Assertions
        assertNotNull(propertiesResult1)
        assertNotNull(propertiesResult2)
        assertEquals(mutableListOf(property1), propertiesResult1)
        assertEquals(mutableListOf(property1, property2), propertiesResult2)

        /*
         * The propertyPoiJoin update is impossible because
         * the "Poi" object have only primary/foreign keys
         */
    }

    /**
     * Testing prepopulateWithPropertyPoiJoins() and getPropertyPoiJoins()
     */
    @Test
    fun testPrepopulateWithPropertyPoiJoinsAndGetPropertyPoiJoins() = runBlocking {
        // Function under test
        var result = propertyPoiJoinDao.getPropertyPoiJoins().first()

        // Initial assertion
        assertTrue(result.isEmpty())

        // Functions under test
        propertyPoiJoinDao.prepopulateWithPropertyPoiJoins(propertyPoiJoins)
        result = propertyPoiJoinDao.getPropertyPoiJoins().first()

        // Final assertions
        assertEquals(6, result.size)
        assertTrue(result.contains(propertyPoiJoin11))
        assertTrue(result.contains(propertyPoiJoin12))
        assertTrue(result.contains(propertyPoiJoin22))
        assertTrue(result.contains(propertyPoiJoin23))
        assertTrue(result.contains(propertyPoiJoin31))
        assertTrue(result.contains(propertyPoiJoin33))
    }

    @Test
    fun testDeletePropertyPoiJoin() = runBlocking {
        // Prepopulate database
        propertyPoiJoinDao.prepopulateWithPropertyPoiJoins(propertyPoiJoins)

        // Initial assertions
        var result = propertyPoiJoinDao.getPropertyPoiJoins().first()
        assertEquals(6, result.size)
        assertTrue(result.contains(propertyPoiJoin12))

        // Function under test
        propertyPoiJoinDao.deletePropertyPoiJoin(propertyPoiJoin12)

        // Final assertions
        result = propertyPoiJoinDao.getPropertyPoiJoins().first()
        assertEquals(5, result.size)
        assertFalse(result.contains(propertyPoiJoin12))
    }

    /**
     * Testing upsertPropertyPoiJoins() and getPropertyPoiJoins()
     */
    @Test
    fun testUpsertAndGetPropertyPoiJoins() = runBlocking {
        // Function under test
        var result = propertyPoiJoinDao.getPropertyPoiJoins().first()

        // Initial assertion
        assertTrue(result.isEmpty())

        // Functions under test (inserting propertyPoiJoins)
        propertyPoiJoinDao.upsertPropertyPoiJoins(propertyPoiJoins.toMutableList())
        result = propertyPoiJoinDao.getPropertyPoiJoins().first()

        // New assertions
        assertEquals(6, result.size)
        assertTrue(result.contains(propertyPoiJoin11))
        assertTrue(result.contains(propertyPoiJoin12))
        assertTrue(result.contains(propertyPoiJoin22))
        assertTrue(result.contains(propertyPoiJoin23))
        assertTrue(result.contains(propertyPoiJoin31))
        assertTrue(result.contains(propertyPoiJoin33))

        /*
         * The propertyPoiJoins update is impossible because
         * the "Poi" object have only primary/foreign keys
         */
    }

    @Test
    fun testDeletePropertyPoiJoins() = runBlocking {
        // Prepopulate database
        propertyPoiJoinDao.prepopulateWithPropertyPoiJoins(propertyPoiJoins)

        // Initial assertions
        var result = propertyPoiJoinDao.getPropertyPoiJoins().first()
        assertEquals(6, result.size)
        assertTrue(result.contains(propertyPoiJoin11))
        assertTrue(result.contains(propertyPoiJoin12))
        assertTrue(result.contains(propertyPoiJoin22))
        assertTrue(result.contains(propertyPoiJoin23))
        assertTrue(result.contains(propertyPoiJoin31))
        assertTrue(result.contains(propertyPoiJoin33))

        // Function under test
        propertyPoiJoinDao.deletePropertyPoiJoins(propertyPoiJoinsToDelete)

        // Final assertions
        result = propertyPoiJoinDao.getPropertyPoiJoins().first()
        assertEquals(3, result.size)
        assertTrue(result.contains(propertyPoiJoin11))
        assertFalse(result.contains(propertyPoiJoin12))
        assertTrue(result.contains(propertyPoiJoin22))
        assertFalse(result.contains(propertyPoiJoin23))
        assertFalse(result.contains(propertyPoiJoin31))
        assertTrue(result.contains(propertyPoiJoin33))
    }


    // ContentProvider

    @Test
    fun testGetPropertyPoiJoinsWithCursor() = runBlocking {
        // Prepopulate database
        propertyPoiJoinDao.prepopulateWithPropertyPoiJoins(propertyPoiJoins)

        // Function under test
        val cursor = propertyPoiJoinDao.getPropertyPoiJoinsWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(6, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(1L, cursor.getLong(0))
        assertEquals("Poi1", cursor.getString(1))
        cursor.moveToPosition(1)
        assertEquals(1L, cursor.getLong(0))
        assertEquals("Poi2", cursor.getString(1))
        cursor.moveToPosition(2)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("Poi2", cursor.getString(1))
        cursor.moveToPosition(3)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("Poi3", cursor.getString(1))
        cursor.moveToPosition(4)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("Poi1", cursor.getString(1))
        cursor.moveToPosition(5)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("Poi3", cursor.getString(1))

        // Close cursor
        cursor.close()
    }

}