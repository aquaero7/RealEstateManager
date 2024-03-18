package com.aquaero.realestatemanager

import android.content.Context
import android.database.Cursor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.addressDao
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.agentDao
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.cursorFields
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.initDatabase
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.initTest
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.photoDao
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.poiDao
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.propertyDao
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.propertyPoiJoinDao
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.queries
import com.aquaero.realestatemanager.AppContentProviderDaoTestUtils.typeDao
import com.aquaero.realestatemanager.model.PoiEnum
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

/**
 * Partial tests (only DAO function called by ContentProvider are tested,
 * not ContentProvider functions)
 */
@RunWith(AndroidJUnit4::class)
class AppContentProviderDaoTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        initDatabase(context = context)
    }

    @After
    fun tearDown() {
        /*
        runBlocking {
            withContext(Dispatchers.IO) {
                testDatabase.clearAllTables()
                testDatabase.close()
            }
        }
        */
    }

    @Test
    fun getEntitiesWhenNoneIsInserted() {
        var cursor: Cursor
        for (query in queries) {
            // Initialize cursor and make asynchronous DAO query
            runBlocking {
                cursor = withContext(Dispatchers.IO) { query }
            }

            // Test assertions
            assertNotNull(cursor)
            assertEquals(0, cursor.count)

            cursor.close()
        }
    }

    @Test
    fun getPropertiesWhenSomeAreInserted() {
        val dao = propertyDao
        val (cursor, saleDates) = initTest(dao = dao) { dao.getPropertiesWithCursor() }
        val (saleDate1, saleDate2, saleDate3) = saleDates

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(saleDate1, cursorFields.second)     // assertNull(cursorSaleDate)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(saleDate2, cursorFields.second)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(saleDate3, cursorFields.second)     // assertNull(cursorSaleDate)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getForSaleProperties() {
        val dao = propertyDao
        val (cursor, saleDates) = initTest(dao = dao) { dao.getPropertiesForSaleWithCursor() }
        val saleDate1 = saleDates.first
        val saleDate3 = saleDates.third

        // Test assertions
        assertNotNull(cursor)
        assertEquals(2, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(saleDate1, cursorFields.second)     // assertNull(cursorSaleDate)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(saleDate3, cursorFields.second)     // assertNull(cursorSaleDate)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getSoldProperties() {
        val dao = propertyDao
        val (cursor, saleDates) = initTest(dao = dao) { dao.getSoldPropertiesWithCursor() }
        val saleDate2 = saleDates.second

        // Test assertions
        assertNotNull(cursor)
        assertEquals(1, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First and only row
                val cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(saleDate2, cursorFields.second)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getAddressesWhenSomeAreInserted() {
        val dao = addressDao
        val cursor = initTest(dao = dao) { dao.getAddressesWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getPhotosWhenSomeAreInserted() {
        val dao = photoDao
        val cursor = initTest(dao = dao) { dao.getPhotosWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getAgentsWhenSomeAreInserted() {
        /*
         * In the application, the database is pre-populated with 4 agents, using AGENT_PREPOPULATION_DATA
         * The test also populates the 4 agents, sorted by lastName then firstName, using AGENT_PREPOPULATION_DATA
         */
        val dao = agentDao
        val cursor = initTest(dao = dao) { dao.getAgentsWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(4, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(4L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor.close()
    }

    @Test
    fun getTypesWhenSomeAreInserted() {
        /*
         * In the application, the database is pre-populated with 9 types, using TYPE_PREPOPULATION_DATA
         * The test also populates the 9 types, sorted by typeId, using TYPE_PREPOPULATION_DATA
         */
        val dao = typeDao
        val cursor = initTest(dao = dao) { dao.getTypesWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(9, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.UNASSIGNED.key, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.CASTLE.key, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.DUPLEX.key, cursorFields.first)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.FLAT.key, cursorFields.first)

                // Fifth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.HOSTEL.key, cursorFields.first)

                // Sixth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.HOUSE.key, cursorFields.first)

                // Seventh row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.LOFT.key, cursorFields.first)

                // Eighth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.MANOR.key, cursorFields.first)

                // Ninth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(TypeEnum.PENTHOUSE.key, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor.close()
    }

    @Test
    fun getPoisWhenSomeAreInserted() {
        /*
         * In the application, the database is pre-populated with 6 pois, using POI_PREPOPULATION_DATA
         * The test also populates the 6 pois, unsorted, using POI_PREPOPULATION_DATA
         */
        val dao = poiDao
        val cursor = initTest(dao = dao) { dao.getPoisWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(6, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(PoiEnum.HOSPITAL.key, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(PoiEnum.SCHOOL.key, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(PoiEnum.RESTAURANT.key, cursorFields.first)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(PoiEnum.SHOP.key, cursorFields.first)

                // Fifth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(PoiEnum.RAILWAY_STATION.key, cursorFields.first)

                // Sixth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(PoiEnum.CAR_PARK.key, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor.close()
    }

    @Test
    fun getPropertyPoiJoinsWhenSomeAreInserted() {
        val dao = propertyPoiJoinDao
        val cursor = initTest(dao = dao) { dao.getPropertyPoiJoinsWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(4, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(PoiEnum.HOSPITAL.key, cursorFields.second)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(PoiEnum.RESTAURANT.key, cursorFields.second)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(PoiEnum.HOSPITAL.key, cursorFields.second)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(PoiEnum.SCHOOL.key, cursorFields.second)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor.close()
    }

}