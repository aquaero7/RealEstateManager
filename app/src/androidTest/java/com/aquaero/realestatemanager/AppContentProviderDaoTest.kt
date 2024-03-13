package com.aquaero.realestatemanager

import android.content.Context
import android.database.Cursor
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.properties.Delegates

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
        runBlocking {
            withContext(Dispatchers.IO) {
                testDatabase.clearAllTables()
                testDatabase.close()
            }
        }
    }

    @Test
    fun getAllPropertiesWhenNoneIsInserted() {
        runBlocking {

            // Initialize cursor and make asynchronous DAO query
            val cursor: Cursor = withContext(Dispatchers.IO) {
                propertyDao.getAllPropertiesWithCursor()
            }

            // Test assertions
            assertNotNull(cursor)
            assertEquals(0, cursor.count)

            // Close cursor
            cursor.close()
        }
    }

    @Test
    fun getAllPropertiesWhenSomeAreInserted() {

        // Initialize entities
        val (type1, agent1, property1) = initEntities()
        val (type2, agent2, property2) = initEntities(propertyId = 2)
        val (type3, agent3, property3) = initEntities(propertyId = 3)
        val saleDate1 = property1.saleDate
        val saleDate2 = property2.saleDate
        val saleDate3 = property3.saleDate

        // Populate database
        runBlocking {

            // Populate database
            populateDatabase(type = type1, agent = agent1, property = property1)
            populateDatabase(type = type2, agent = agent2, property = property2)
            populateDatabase(type = type3, agent = agent3, property = property3)

            // // Initialize cursor and make asynchronous DAO query
            val cursor: Cursor = withContext(Dispatchers.IO) {
                propertyDao.getAllPropertiesWithCursor()
            }

            // Test assertions

            assertNotNull(cursor)
            assertEquals(3, cursor.count)

            if (cursor.moveToFirst()) {
                do {
                    // First row
                    var cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    var cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(1, cursorPropId)
                    assertEquals(saleDate1, cursorSaleDate)     // assertNull(cursorSaleDate)

                    // Second row
                    cursor.moveToNext()
                    cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(2, cursorPropId)
                    assertEquals(saleDate2, cursorSaleDate)

                    // Third row
                    cursor.moveToNext()
                    cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(3, cursorPropId)
                    assertEquals(saleDate3, cursorSaleDate)     // assertNull(cursorSaleDate)
                } while (cursor.moveToNext())
            } else {
                fail("Cursor is empty")
            }

            // Close cursor
            cursor.close()
        }
    }

    @Test
    fun getForSaleProperties() {

        // Initialize entities
        val (type1, agent1, property1) = initEntities()
        val (type2, agent2, property2) = initEntities(propertyId = 2)
        val (type3, agent3, property3) = initEntities(propertyId = 3)
        val saleDate1 = property1.saleDate
        val saleDate2 = property2.saleDate
        val saleDate3 = property3.saleDate

        // Populate database
        runBlocking {

            // Populate database
            populateDatabase(type = type1, agent = agent1, property = property1)
            populateDatabase(type = type2, agent = agent2, property = property2)
            populateDatabase(type = type3, agent = agent3, property = property3)

            // // Initialize cursor and make asynchronous DAO query
            val cursor: Cursor = withContext(Dispatchers.IO) {
                propertyDao.getPropertiesForSaleWithCursor()
            }

            // Test assertions

            assertNotNull(cursor)
            assertEquals(2, cursor.count)

            if (cursor.moveToFirst()) {
                do {
                    // First row
                    var cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    var cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(1, cursorPropId)
                    assertEquals(saleDate1, cursorSaleDate)     // assertNull(cursorSaleDate)

                    // Second row
                    cursor.moveToNext()
                    cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(3, cursorPropId)
                    assertEquals(saleDate3, cursorSaleDate)     // assertNull(cursorSaleDate)
                } while (cursor.moveToNext())
            } else {
                fail("Cursor is empty")
            }

            // Close cursor
            cursor.close()
        }
    }

    @Test
    fun getSoldProperties() {

        // Initialize entities
        val (type1, agent1, property1) = initEntities()
        val (type2, agent2, property2) = initEntities(propertyId = 2)
        val (type3, agent3, property3) = initEntities(propertyId = 3)
        val saleDate1 = property1.saleDate
        val saleDate2 = property2.saleDate
        val saleDate3 = property3.saleDate

        // Populate database
        runBlocking {

            // Populate database
            populateDatabase(type = type1, agent = agent1, property = property1)
            populateDatabase(type = type2, agent = agent2, property = property2)
            populateDatabase(type = type3, agent = agent3, property = property3)

            // // Initialize cursor and make asynchronous DAO query
            val cursor: Cursor = withContext(Dispatchers.IO) {
                propertyDao.getSoldPropertiesWithCursor()
            }

            // Test assertions

            assertNotNull(cursor)
            assertEquals(1, cursor.count)

            if (cursor.moveToFirst()) {
                do {
                    // First row
                    var cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    var cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(2, cursorPropId)
                    assertEquals(saleDate2, cursorSaleDate)
                } while (cursor.moveToNext())
            } else {
                fail("Cursor is empty")
            }

            // Close cursor
            cursor.close()
        }
    }


    /**/

    private fun initDatabase() {
        testDatabase = Room
            .inMemoryDatabaseBuilder(context = context, klass =  AppDatabase::class.java)
//            .allowMainThreadQueries()
            .build()
        typeDao = testDatabase.typeDao
        agentDao = testDatabase.agentDao
        propertyDao = testDatabase.propertyDao
    }

    private fun initEntities(propertyId: Long = 1): Triple<Type, Agent, Property> {
        val saleDate1 = null
        val saleDate2 = "2024-03-01"
        val saleDate3 = null
        val type1 = Type(typeId = TypeEnum.FLAT.key)
        val type2 = Type(typeId = TypeEnum.DUPLEX.key)
        val type3 = Type(typeId = TypeEnum.HOUSE.key)
        val agent1 = Agent(agentId = 1, firstName = "firstName1", lastName = "name1")
        val agent2 = Agent(agentId = 2, firstName = "firstName2", lastName = "name2")
        val agent3 = Agent(agentId = 3, firstName = "firstName3", lastName = "name3")

        val property1 =
            Property(
                propertyId = 1, typeId = type1.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate1, agentId = agent1.agentId
            )
        val property2 =
            Property(
                propertyId = 2, typeId = type2.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate2, agentId = agent2.agentId
            )
        val property3 =
            Property(
                propertyId = 3, typeId = type3.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate3, agentId = agent3.agentId
            )

        return when (propertyId) {
            1L -> Triple(first = type1, second = agent1, third = property1)
            2L -> Triple(first = type2, second = agent2, third = property2)
            3L -> Triple(first = type3, second = agent3, third = property3)
            else -> Triple(first = type1, second = agent1, third = property1)
        }
    }

    private suspend fun populateDatabase(type: Type, agent: Agent, property: Property) {
        withContext(Dispatchers.IO) {
            typeDao.upsertType(type = type)
            agentDao.upsertAgent(agent = agent)
            propertyDao.upsertProperty(property = property)
        }
    }

}