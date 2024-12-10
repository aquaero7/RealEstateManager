package com.aquaero.realestatemanager.dao_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
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
@Config(manifest= Config.NONE)
/**
 * Testing PoiDao
 */
class PoiDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao

    private lateinit var poi1: Poi
    private lateinit var poi2: Poi
    private lateinit var poi3: Poi
    private lateinit var pois: List<Poi>

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

        poi1 = Poi("Poi1")
        poi2 = Poi("Poi2")
        poi3 = Poi("Poi3")

        pois = listOf(poi2, poi3, poi1)
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
     * Testing upsertPoi() and getPoi()
     */
    @Test
    fun testUpsertAndGetPoi() = runBlocking {
        // Function under test (inserting poi)
        poiDao.upsertPoi(poi1)

        // Get the poi from database to check the insertion (other function under test)
        val result = poiDao.getPoi("Poi1").first()

        // Assertions
        assertNotNull(result)
        assertEquals(poi1.poiId, result.poiId)

        // The poi update is impossible because the poi ID is the only property of the "Poi" object
    }

    /**
     * Testing prepopulateWithPois() and getPois()
     */
    @Test
    fun testPrepopulateWithPoisAndGetPois() = runBlocking {
        // Function under test
        var result = poiDao.getPois().first()

        // Initial assertions
        assertEquals(6, result.size)    // Pre-populated in setup
        assertFalse(result.contains(poi1))
        assertFalse(result.contains(poi2))
        assertFalse(result.contains(poi3))

        // Functions under test
        poiDao.prepopulateWithPois(pois)  // Add 3 pois to the first six pre-populated in setup
        result = poiDao.getPois().first()

        // Final assertions
        assertEquals(9, result.size)
        assertTrue(result.contains(poi1))
        assertTrue(result.contains(poi2))
        assertTrue(result.contains(poi3))
    }

    @Test
    fun testDeletePoi() = runBlocking {
        // Prepopulate database
        poiDao.prepopulateWithPois(pois)  // Add 3 pois to the first six pre-populated in setup

        // Initial assertions
        var result = poiDao.getPois().first()
        assertEquals(9, result.size)
        assertTrue(result.contains(poi2))

        // Function under test
        poiDao.deletePoi(poi2)

        // Final assertions
        result = poiDao.getPois().first()
        assertEquals(8, result.size)
        assertFalse(result.contains(poi2))
    }

    @Test
    fun testGetPoisOrderedById() = runBlocking {
        // Function under test
        val result = poiDao.getPoisOrderedById().first()

        // Assertions
        assertEquals(6, result.size)
        assertEquals("car_park", result[0].poiId)
        assertEquals("hospital", result[1].poiId)
        assertEquals("railway_station", result[2].poiId)
        assertEquals("restaurant", result[3].poiId)
        assertEquals("school", result[4].poiId)
        assertEquals("shop", result[5].poiId)
    }


    // ContentProvider

    @Test
    fun testGetPoisWithCursor() = runBlocking {
        // Function under test
        val cursor = poiDao.getPoisWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(6, cursor.count)
        cursor.moveToPosition(0)
        assertEquals("car_park", cursor.getString(0))
        cursor.moveToPosition(1)
        assertEquals("hospital", cursor.getString(0))
        cursor.moveToPosition(2)
        assertEquals("railway_station", cursor.getString(0))
        cursor.moveToPosition(3)
        assertEquals("restaurant", cursor.getString(0))
        cursor.moveToPosition(4)
        assertEquals("school", cursor.getString(0))
        cursor.moveToPosition(5)
        assertEquals("shop", cursor.getString(0))

        // Close cursor
        cursor.close()
    }

}