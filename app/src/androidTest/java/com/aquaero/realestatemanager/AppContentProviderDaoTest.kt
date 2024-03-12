package com.aquaero.realestatemanager

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Partial tests (only DAO function called by ContentProvider are tested,
 * not ContentProvider functions)
 */
@RunWith(AndroidJUnit4::class)
class AppContentProviderDaoTest {

    private lateinit var context: Context
    private lateinit var testDatabase: AppDatabase
    private lateinit var typeDao: TypeDao
    private lateinit var agentDao: AgentDao
    private lateinit var propertyDao: PropertyDao

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        initDatabase()
    }

    @After
    fun tearDown() {
        testDatabase.close()
    }

    @Test
    fun getItemsWhenNoItemInserted() {

        // Initialize cursor
        val cursor: Cursor = propertyDao.getAllPropertiesWithCursor()

        // Test assertions
        assertNotNull(cursor)
        assertEquals(0, cursor.count)

        // Close cursor
        cursor.close()
    }

    @Test
    fun getItemsWhenOneItemInserted() {

        // Initialize entities
        val (type1, agent1, property1) = initEntities()
        val saleDate1 = property1.saleDate

        // Populate database
        runBlocking {
            populateDatabase(type1, agent1, property1)
        }

        // Initialize cursor and query DAO
        val cursor: Cursor = propertyDao.getAllPropertiesWithCursor()

        // Test assertions
        assertNotNull(cursor)
        assertEquals(1, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                val cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                val cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                assertEquals(1, cursorPropId)
                assertEquals(saleDate1, cursorSaleDate)
//                assertNull(saleDate)
            } while (cursor.moveToNext())
        } else {
//            fail("Cursor is empty")
            Log.d("Test", "Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }



    /**/

    private fun initDatabase() {
        testDatabase = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
//            .allowMainThreadQueries()
            .build()
        typeDao = testDatabase.typeDao
        agentDao = testDatabase.agentDao
        propertyDao = testDatabase.propertyDao
    }

    private fun initEntities(): Triple<Type, Agent, Property> {
        val saleDate = "2024-03-01"
        val type1 = Type(TypeEnum.UNASSIGNED.key)
        val agent1 = Agent(1, "firstName1", "name1")

        val property1 =
            Property(
                1, TypeEnum.UNASSIGNED.key, null, null, null, null,
                null, null, null, null, saleDate, 1
            )

        return Triple(type1, agent1, property1)
    }

    private suspend fun populateDatabase(type: Type, agent: Agent, property: Property) {
        typeDao.upsertType(type)
        agentDao.upsertAgent(agent)
        propertyDao.upsertProperty(property)
    }

}