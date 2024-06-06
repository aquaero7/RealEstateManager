package com.aquaero.realestatemanager

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import androidx.test.core.app.ApplicationProvider
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.cursorAndQuery
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.cursorFields
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.initDatabase
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.initTest
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.projections
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriAddresses
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriAgents
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriPhotos
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriPois
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriProperties
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriPropertiesForSale
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriPropertyPoiJoins
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriSoldProperties
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uriTypes
import com.aquaero.realestatemanager.utils.ContentProviderTestUtils.uris
import com.aquaero.realestatemanager.model.PoiEnum
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.provider.AppContentProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppContentProviderTest {

    private lateinit var testContext: Context
    private lateinit var contentResolver: ContentResolver

    @Before
    fun setUp() {
        testContext = ApplicationProvider.getApplicationContext()
        Robolectric.setupContentProvider(AppContentProvider::class.java)
        contentResolver = testContext.contentResolver
        initDatabase(testContext)
    }

    @After
    fun tearDown() {}

    @Test
    fun getEntitiesWhenNoneIsInserted() {
        var cursor: Cursor?
        for (uri in uris) {
            val projection = projections.elementAt(uris.indexOf(uri))

            // Initialize cursor and make asynchronous query
            runBlocking {
                cursor = cursorAndQuery(contentResolver = contentResolver, uri = uri, projection = projection)
            }

            // Test assertions
            assertNotNull(cursor)
            assertEquals(0, cursor?.count)

            cursor?.close()
        }
    }

    @Test
    fun getPropertiesWhenSomeAreInserted() {
        val (cursor, saleDates) = initTest(contentResolver = contentResolver, uri = uriProperties)
        val (saleDate1, saleDate2, saleDate3) = saleDates

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriProperties, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(saleDate1, cursorFields.second)     // assertNull(saleDate)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriProperties, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(saleDate2, cursorFields.second)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriProperties, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(saleDate3, cursorFields.second)     // assertNull(saleDate)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getPropertiesForSale() {
        val (cursor, saleDates) = initTest(contentResolver = contentResolver, uri = uriPropertiesForSale)
        val saleDate1 = saleDates.first
        val saleDate3 = saleDates.third

        // Test assertions
        assertNotNull(cursor)
        assertEquals(2, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriPropertiesForSale, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(saleDate1, cursorFields.second)     // assertNull(saleDate)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPropertiesForSale, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(saleDate3, cursorFields.second)     // assertNull(saleDate)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getSoldProperties() {
        val (cursor, saleDates) = initTest(contentResolver = contentResolver, uri = uriSoldProperties)
        val saleDate2 = saleDates.second

        // Test assertions
        assertNotNull(cursor)
        assertEquals(1, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First and only row
                val cursorFields = cursorFields(uri = uriSoldProperties, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(saleDate2, cursorFields.second)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getAddressesWhenSomeAreInserted() {
        val cursor = initTest(contentResolver = contentResolver, uri = uriAddresses).first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriAddresses, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriAddresses, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriAddresses, cursor = cursor)

                assertEquals(3L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getPhotosWhenSomeAreInserted() {
        val cursor = initTest(contentResolver = contentResolver, uri = uriPhotos).first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriPhotos, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPhotos, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPhotos, cursor = cursor)

                assertEquals(3L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getAgentsWhenSomeAreInserted() {
        /*
         * In the application, the database is pre-populated with 4 agents, using AGENT_PREPOPULATION_DATA
         * The test also populates the 4 agents, sorted by lastName then firstName, using AGENT_PREPOPULATION_DATA
         */
        val cursor = initTest(contentResolver = contentResolver, uri = uriAgents).first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(4, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriAgents, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriAgents, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriAgents, cursor = cursor)

                assertEquals(3L, cursorFields.first)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriAgents, cursor = cursor)

                assertEquals(4L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getTypesWhenSomeAreInserted() {
        /*
         * In the application, the database is pre-populated with 9 types, using TYPE_PREPOPULATION_DATA
         * The test also populates the 9 types, sorted by typeId, using TYPE_PREPOPULATION_DATA
         */
        val cursor = initTest(contentResolver = contentResolver, uri = uriTypes).first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(9, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.UNASSIGNED.key, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.CASTLE.key, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.DUPLEX.key, cursorFields.first)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.FLAT.key, cursorFields.first)

                // Fifth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.HOSTEL.key, cursorFields.first)

                // Sixth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.HOUSE.key, cursorFields.first)

                // Seventh row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.LOFT.key, cursorFields.first)

                // Eighth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.MANOR.key, cursorFields.first)

                // Ninth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriTypes, cursor = cursor)

                assertEquals(TypeEnum.PENTHOUSE.key, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getPoisWhenSomeAreInserted() {
        /*
         * In the application, the database is pre-populated with 6 pois, using POI_PREPOPULATION_DATA
         * The test also populates the 6 pois, unsorted, using POI_PREPOPULATION_DATA
         */
        val cursor = initTest(contentResolver = contentResolver, uri = uriPois).first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(6, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriPois, cursor = cursor)

                assertEquals(PoiEnum.HOSPITAL.key, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPois, cursor = cursor)

                assertEquals(PoiEnum.SCHOOL.key, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPois, cursor = cursor)

                assertEquals(PoiEnum.RESTAURANT.key, cursorFields.first)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPois, cursor = cursor)

                assertEquals(PoiEnum.SHOP.key, cursorFields.first)

                // Fifth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPois, cursor = cursor)

                assertEquals(PoiEnum.RAILWAY_STATION.key, cursorFields.first)

                // Sixth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPois, cursor = cursor)

                assertEquals(PoiEnum.CAR_PARK.key, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

    @Test
    fun getPropertyPoiJoinsWhenSomeAreInserted() {
        val cursor = initTest(contentResolver = contentResolver, uri = uriPropertyPoiJoins).first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(4, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(uri = uriPropertyPoiJoins, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(PoiEnum.HOSPITAL.key, cursorFields.second)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPropertyPoiJoins, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(PoiEnum.RESTAURANT.key, cursorFields.second)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPropertyPoiJoins, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(PoiEnum.HOSPITAL.key, cursorFields.second)

                // Fourth row
                cursor.moveToNext()
                cursorFields = cursorFields(uri = uriPropertyPoiJoins, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(PoiEnum.SCHOOL.key, cursorFields.second)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        cursor?.close()
    }

}