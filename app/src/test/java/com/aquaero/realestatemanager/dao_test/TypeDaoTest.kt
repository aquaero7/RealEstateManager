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
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Type
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
 * Testing TypeDao
 */
class TypeDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao

    private lateinit var type1: Type
    private lateinit var type2: Type
    private lateinit var type3: Type
    private lateinit var types: List<Type>

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

        type1 = Type("Type1")
        type2 = Type("Type2")
        type3 = Type("Type3")

        types = listOf(type2, type3, type1)
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
     * Testing upsertType() and getType()
     */
    @Test
    fun testUpsertAndGetType() = runBlocking {
        // Function under test (inserting type)
        typeDao.upsertType(type1)

        // Get the type from database to check the insertion (other function under test)
        val result = typeDao.getType("Type1").first()

        // Assertions
        assertNotNull(result)
        assertEquals(type1.typeId, result.typeId)

        // The type update is impossible because the type ID is the only property of the "Type" object
    }

    /**
     * Testing prepopulateWithTypes() and getTypes()
     */
    @Test
    fun testPrepopulateWithTypesAndGetTypes() = runBlocking {
        // Function under test
        var result = typeDao.getTypes().first()

        // Initial assertions
        assertEquals(9, result.size)    // Pre-populated in setup
        assertFalse(result.contains(type1))
        assertFalse(result.contains(type2))
        assertFalse(result.contains(type3))

        // Functions under test
        typeDao.prepopulateWithTypes(types)  // Add 3 types to the first nine pre-populated in setup
        result = typeDao.getTypes().first()

        // Final assertions
        assertEquals(12, result.size)
        assertTrue(result.contains(type1))
        assertTrue(result.contains(type2))
        assertTrue(result.contains(type3))
    }

    @Test
    fun testDeleteType() = runBlocking {
        // Prepopulate database
        typeDao.prepopulateWithTypes(types)  // Add 3 types to the first nine pre-populated in setup

        // Initial assertions
        var result = typeDao.getTypes().first()
        assertEquals(12, result.size)
        assertTrue(result.contains(type2))

        // Function under test
        typeDao.deleteType(type2)

        // Final assertions
        result = typeDao.getTypes().first()
        assertEquals(11, result.size)
        assertFalse(result.contains(type2))
    }

    @Test
    fun testGetTypesOrderedById() = runBlocking {
        // Function under test
        val result = typeDao.getTypesOrderedById().first()

        // Assertions
        assertEquals(9, result.size)
        assertEquals("_unassigned_", result[0].typeId)
        assertEquals("castle", result[1].typeId)
        assertEquals("duplex", result[2].typeId)
        assertEquals("flat", result[3].typeId)
        assertEquals("hostel", result[4].typeId)
        assertEquals("house", result[5].typeId)
        assertEquals("loft", result[6].typeId)
        assertEquals("manor", result[7].typeId)
        assertEquals("penthouse", result[8].typeId)
    }


    // ContentProvider

    @Test
    fun testGetTypesWithCursor() = runBlocking {
        // Function under test
        val cursor = typeDao.getTypesWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(9, cursor.count)
        cursor.moveToPosition(0)
        assertEquals("_unassigned_", cursor.getString(0))
        cursor.moveToPosition(1)
        assertEquals("castle", cursor.getString(0))
        cursor.moveToPosition(2)
        assertEquals("duplex", cursor.getString(0))
        cursor.moveToPosition(3)
        assertEquals("flat", cursor.getString(0))
        cursor.moveToPosition(4)
        assertEquals("hostel", cursor.getString(0))
        cursor.moveToPosition(5)
        assertEquals("house", cursor.getString(0))
        cursor.moveToPosition(6)
        assertEquals("loft", cursor.getString(0))
        cursor.moveToPosition(7)
        assertEquals("manor", cursor.getString(0))
        cursor.moveToPosition(8)
        assertEquals("penthouse", cursor.getString(0))


        // Close cursor
        cursor.close()
    }

}